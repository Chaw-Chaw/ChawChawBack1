package okky.team.chawchaw.chat.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUserEntity, Long> {

    List<ChatRoomUserEntity> findAllByUserId(Long userId);
    List<ChatRoomUserEntity> findAllByChatRoomId(Long roomId);

    @Query( "select count(ru.chatRoom) > 0 " +
            "from ChatRoomUserEntity ru " +
            "where ru.user.id = :userId or ru.user.id = :userId2 " +
            "group by ru.chatRoom " +
            "having count(ru.chatRoom) > 1")
    Boolean isChatRoom(Long userId, Long userId2);

}
