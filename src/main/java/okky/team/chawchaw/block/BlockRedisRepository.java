package okky.team.chawchaw.block;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class BlockRedisRepository {

    private final RedisTemplate redisTemplate;

    public void save(Set<Long> blockUserIds, String email) {
        String key = "block::" + email;
        redisTemplate.opsForValue().set(key, blockUserIds);
    }

    public void update(Set<Long> blockUserIds, String email) {
        String key = "block::" + email;
        redisTemplate.opsForValue().setIfPresent(key, blockUserIds);
    }

    public void delete(String email) {
        String key = "block::" + email;
        redisTemplate.opsForValue().set(key, null, 1, TimeUnit.MILLISECONDS);
    }

    public boolean isBlock(String email) {
        String key = "block::" + email;
        Object value = redisTemplate.opsForValue().get(key);
        return value != null;
    }

}

