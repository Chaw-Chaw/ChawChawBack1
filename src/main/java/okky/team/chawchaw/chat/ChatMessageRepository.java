package okky.team.chawchaw.chat;

import okky.team.chawchaw.chat.dto.ChatMessageDto;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ChatMessageRepository {

    @Resource(name = "redisTemplate")
    private ListOperations<Long, ChatMessageDto> listOperations;

    public void save(ChatMessageDto message) {
        listOperations.rightPush(message.getRoomId(), message);
    }

    public List<ChatMessageDto> findAll(Long roomId) {
        return listOperations.range(roomId, 0, -1);
    }

    public List<ChatMessageDto> findAllOrderByRegDateDesc(Long roomId) {
        return listOperations.range(roomId, 0, -1).stream()
                .sorted(Comparator.comparing(ChatMessageDto::getRegDate).reversed())
                .collect(Collectors.toList());
    }

    public void remove(Long roomId) { listOperations.trim(roomId, -1, 0); }

}
