package okky.team.chawchaw.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class TokenRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void save(Long userId, String token) {
        String key = "token::" + userId;
        redisTemplate.opsForValue().set(key, token, 30, TimeUnit.MINUTES);
    }

    public String findByUserId(Long userId) {
        String key = "token::" + userId;
        Object result = redisTemplate.opsForValue().get(key);
        return result != null ? result.toString() : "";
    }

    public void delete(Long userId) {
        String key = "token::" + userId;
        redisTemplate.opsForValue().set(key, "", 1, TimeUnit.MILLISECONDS);
    }

}
