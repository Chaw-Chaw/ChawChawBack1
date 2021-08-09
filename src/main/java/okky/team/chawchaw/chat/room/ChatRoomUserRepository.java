package okky.team.chawchaw.chat.room;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUserEntity, Long> {
}
