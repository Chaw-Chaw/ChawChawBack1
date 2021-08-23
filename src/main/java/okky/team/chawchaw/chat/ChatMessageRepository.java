package okky.team.chawchaw.chat;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {

    private final RedisTemplate redisTemplate;

    public void save(ChatMessageDto message) {
        String key = "message::" + message.getRoomId().toString() + "_" + UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(key, message);
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
        keys.stream().forEach(x -> redisTemplate.opsForValue().set(x, "", 1, TimeUnit.MILLISECONDS));
    }

}
