package okky.team.chawchaw.chat.room;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUserEntity, Long> {

    List<ChatRoomUserEntity> findAllByUserFromId(Long userId);

}
