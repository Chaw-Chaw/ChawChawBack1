package okky.team.chawchaw.like;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.config.properties.RedisProperties;
import okky.team.chawchaw.like.dto.LikeMessageDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class LikeRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisProperties redisProperties;

    public void save(LikeMessageDto messageDto, Long userId) {
        String key = redisProperties.getLike().getPrefix() + userId + "_" + UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(key, messageDto, redisProperties.getLike().getExpirationTime(), TimeUnit.SECONDS);
    }

    public List<LikeMessageDto> findMessagesByUserFromId(Long userFromId) {
        Set<String> keys = redisTemplate.keys(redisProperties.getLike().getPrefix() + userFromId + "_" + "*");
        List<LikeMessageDto> result = keys.stream().map(x -> (LikeMessageDto) redisTemplate.opsForValue().get(x)).collect(Collectors.toList());
        return result;
    }

    public void deleteMessagesByUserFromId(Long userFromId) {
        Set<String> keys = redisTemplate.keys(redisProperties.getLike().getPrefix() + userFromId + "_" + "*");
        keys.stream().forEach(x -> redisTemplate.opsForValue().set(x, "", 1, TimeUnit.MILLISECONDS));
    }

}
