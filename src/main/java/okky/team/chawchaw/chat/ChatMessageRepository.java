package okky.team.chawchaw.chat;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {

    private final RedisTemplate redisTemplate;
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

    public List<ChatMessageDto> findAllByRoomIdOrderByRegDateAsc(Long roomId) {
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

}
