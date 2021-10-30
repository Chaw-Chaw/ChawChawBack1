package okky.team.chawchaw.chat.room;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.user.UserEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    @Transactional(readOnly = false)
    public Long findRoomIdByUserIds(Long userId, Long userId2) {
        Long roomId = chatRoomUserRepository.findChatRoomIdByUserIds(userId, userId2);
        List<ChatRoomUserEntity> roomUsers = chatRoomUserRepository.findAllByChatRoomId(roomId);
        for (ChatRoomUserEntity roomUser : roomUsers) {
            if (roomUser.getUser().getId().equals(userId) && roomUser.getIsExit()) {
                roomUser.enterRoom();
                break;
            }
        }
        return roomId == null ? -1L : roomId;
    }

}
