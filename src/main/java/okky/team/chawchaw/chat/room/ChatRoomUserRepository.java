package okky.team.chawchaw.chat.room;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUserEntity, Long> {

    List<ChatRoomUserEntity> findAllByUserId(Long userId);
    @EntityGraph(attributePaths = {"chatRoom", "user"})
    List<ChatRoomUserEntity> findAllByChatRoomId(Long roomId);

    void deleteByUserId(Long userId);

    @Query("select ru.chatRoom.id " +
            "from ChatRoomUserEntity ru " +
            "where ru.user.id = :userId or ru.user.id = :userId2 " +
            "group by ru.chatRoom " +
            "having count(ru.chatRoom) = 2")
    Long findChatRoomIdByUserIds(@Param("userId") Long userId, @Param("userId2") Long userId2);

    @Query("select ru.chatRoom " +
            "from ChatRoomUserEntity ru " +
            "where ru.user.id = :userId")
    List<ChatRoomEntity> findChatRoomsByUserId(@Param("userId") Long userId);

}
