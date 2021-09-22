package okky.team.chawchaw.like;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.like.dto.LikeMessageDto;
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
public class LikeMessageRepository {

    private final RedisTemplate redisTemplate;

    public void save(LikeMessageDto messageDto, Long userId) {
        String key = "like::" + userId + "_" + UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(key, messageDto);
        redisTemplate.expireAt(key, Date.from(ZonedDateTime.now().plusDays(7).toInstant()));
    }

    public List<LikeMessageDto> findMessagesByUserId(Long userId) {
        Set<String> keys = redisTemplate.keys("like::" + userId + "_" + "*");
        List<LikeMessageDto> result = keys.stream().map(x -> (LikeMessageDto) redisTemplate.opsForValue().get(x)).collect(Collectors.toList());
        return result;
    }

    public void deleteMessagesByUserId(Long userId) {
        Set<String> keys = redisTemplate.keys("like::" + userId + "_" + "*");
        keys.stream().forEach(x -> redisTemplate.opsForValue().set(x, "", 1, TimeUnit.MILLISECONDS));
    }

}
