package okky.team.chawchaw.follow;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.follow.dto.FollowMessageDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FollowMessageRepository {

    private final RedisTemplate redisTemplate;

    public void save(FollowMessageDto messageDto, Long userId) {
        String key = "follow::" + userId + "_" + UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(key, messageDto);
        redisTemplate.expireAt(key, Date.from(ZonedDateTime.now().plusDays(7).toInstant()));
    }

    public List<FollowMessageDto> findMessagesByUserId(Long userId) {
        Set<String> keys = redisTemplate.keys("follow::" + userId + "_" + "*");
        keys.forEach(System.out::println);
        List<FollowMessageDto> result = keys.stream().map(x -> (FollowMessageDto) redisTemplate.opsForValue().get(x)).collect(Collectors.toList());
        return result;
    }

    public void deleteMessagesByUserId(Long userId) {
        Set<String> keys = redisTemplate.keys("follow::" + userId + "_" + "*");
        keys.forEach(System.out::println);
        keys.stream().forEach(x -> redisTemplate.opsForValue().set(x, "", 1, TimeUnit.MILLISECONDS));
    }

}
