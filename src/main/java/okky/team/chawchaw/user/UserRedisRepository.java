package okky.team.chawchaw.user;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.config.properties.RedisProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class UserRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisProperties redisProperties;

    public Set<String> findViewsKeys() {
        return redisTemplate.keys("views::*");
    }

    public Long findViewsByUserId(Long userId) {
        return (Long) redisTemplate.opsForValue().get(redisProperties.getViews().getPrefix() + userId);
    }

    public void updateViews(Long userId, Long views) {
        redisTemplate.opsForValue().set(redisProperties.getViews().getPrefix() + userId, views,
                redisProperties.getViews().getExpirationTime(), TimeUnit.SECONDS);
    }

    public Boolean findViewDuplexByUserIds(Long userFromId, Long userToId) {
        if (redisTemplate.opsForValue().get(redisProperties.getViewDuplex().getPrefix() + userFromId + "_" + userToId) == null)
            return false;
        else
            return true;
    }

    public void updateViewDuplex(Long userFromId, Long userToId) {
        redisTemplate.opsForValue().set(redisProperties.getViewDuplex().getPrefix() + userFromId + "_" + userToId, true,
                redisProperties.getViewDuplex().getExpirationTime(), TimeUnit.SECONDS);
    }

}
