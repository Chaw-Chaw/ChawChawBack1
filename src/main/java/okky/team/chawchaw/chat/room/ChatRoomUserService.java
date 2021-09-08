package okky.team.chawchaw.chat.room;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.user.UserEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomUserService {

    private final ChatRoomUserRepository chatRoomUserRepository;

    @Cacheable(value = "roomUserIds", key = "#roomId")
    public Map<Long, String> findUserIdAndEmailByChatRoomId(Long roomId) {
        return chatRoomUserRepository.findAllByChatRoomId(roomId).stream()
                .map(ChatRoomUserEntity::getUser)
                .collect(Collectors.toMap(
                        UserEntity::getId,
                        UserEntity::getEmail
                ));
    }

}
