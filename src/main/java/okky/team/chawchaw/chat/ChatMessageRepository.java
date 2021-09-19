package okky.team.chawchaw.chat;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.block.BlockRedisRepository;
import okky.team.chawchaw.block.dto.BlockSessionDto;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.room.ChatRoomUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {

    private final RedisTemplate redisTemplate;
    private final BlockRedisRepository blockRedisRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void save(ChatMessageDto message) {
        String key = "message::" + message.getRoomId().toString() + "_" + UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(key, message);
        if (!message.getMessageType().equals(MessageType.ENTER))
            redisTemplate.expireAt(key, Date.from(ZonedDateTime.now().plusDays(1).toInstant()));
    }

    public List<ChatMessageDto> findAllByRoomId(Long roomId) {
        Set<String> keys = redisTemplate.keys("message::" + roomId.toString() + "_" + "*");
        List<ChatMessageDto> result = keys.stream().map(x -> (ChatMessageDto) redisTemplate.opsForValue().get(x)).collect(Collectors.toList());
        return result;
    }

    public List<ChatMessageDto> findAllByRoomIdOrderByRegDateDesc(Long roomId) {
        Set<String> keys = redisTemplate.keys("message::" + roomId.toString() + "_" + "*");
        List<ChatMessageDto> result = keys.stream().map(x -> (ChatMessageDto) redisTemplate.opsForValue().get(x))
                .sorted(Comparator.comparing(ChatMessageDto::getRegDate))
                .collect(Collectors.toList());
        return result;
    }

    public void deleteByRoomId(Long roomId) {
        Set<String> keys = redisTemplate.keys("message::" + roomId.toString() + "_" + "*");
        for (String key : keys) {
            ChatMessageDto chatMessageDto = (ChatMessageDto) redisTemplate.opsForValue().get(key);
            if (chatMessageDto.getMessageType().equals(MessageType.IMAGE)) {
                String[] splitUrl = chatMessageDto.getMessage().split("/");
                String imageUrl = splitUrl[splitUrl.length - 1];
                amazonS3.deleteObject(new DeleteObjectRequest(bucket, imageUrl));
            }
        }
        keys.stream().forEach(x -> redisTemplate.opsForValue().set(x, "", 1, TimeUnit.MILLISECONDS));
    }

    public Boolean isSession(String email, Long roomId) {
        Object currentRoomId = redisTemplate.opsForValue().get("ws::" + email);
        if (currentRoomId == null || !currentRoomId.equals(roomId)) {
            return false;
        }
        return true;
    }

    @Cacheable(value = "ws", key = "#email")
    public Long createSession(String email) {
        return -1L;
    }

    @CachePut(value = "ws", key = "#email")
    public Long updateSession(String email, Long roomId, Long userId) throws Exception {
        List<BlockSessionDto> blockUsers = blockRedisRepository.findAllByEmail(email);
        LocalDateTime regDate = LocalDateTime.now();
        Long receiveId = chatRoomUserRepository.findAllByChatRoomId(roomId).stream()
                .map(x -> x.getUser().getId())
                .filter(x -> !x.equals(userId))
                .findFirst().orElse(null);
        if (receiveId != null) {
            BlockSessionDto blockUser = blockUsers.stream()
                    .filter(x -> x.getUserId().equals(receiveId))
                    .findFirst().orElse(null);
            if (blockUser != null)
                regDate = blockUser.getRegDate();
        }

        if (redisTemplate.opsForValue().get("ws::" + email) != null) {
            redisTemplate.opsForValue().set("ws::" + email, roomId);
            Set<String> keys = redisTemplate.keys("message::" + roomId.toString() + "_" + "*");

            for (String key : keys) {
                ChatMessageDto message = (ChatMessageDto) redisTemplate.opsForValue().get(key);
                if (message.getIsRead().equals(false) && !message.getSenderId().equals(userId) && message.getRegDate().isBefore(regDate)) {
                    message.setIsRead(true);
                    redisTemplate.opsForValue().set(key, message, ChronoUnit.SECONDS.between(LocalDateTime.now(), message.getRegDate().plusDays(1)), TimeUnit.SECONDS);
                }
            }
            return roomId;
        }
        else
            throw new Exception();
    }

    @CacheEvict(value = "ws", key = "#email")
    public void deleteSession(String email) {}

}
