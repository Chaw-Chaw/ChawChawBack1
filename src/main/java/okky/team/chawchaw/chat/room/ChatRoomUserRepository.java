package okky.team.chawchaw.chat.room;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUserEntity, Long> {

    List<ChatRoomUserEntity> findAllByUserId(Long userId);
    @EntityGraph(attributePaths = {"chatRoom", "user"})
    List<ChatRoomUserEntity> findAllByChatRoomId(Long roomId);

    void deleteByUserId(Long userId);

    @Query( "select ru.chatRoom.id " +
            "from ChatRoomUserEntity ru " +
            "where ru.user.id = :userId or ru.user.id = :userId2 " +
            "group by ru.chatRoom " +
            "having count(ru.chatRoom) = 2")
    Long findChatRoomIdByUserIds(Long userId, Long userId2);

}
