package okky.team.chawchaw.chat.room;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import okky.team.chawchaw.user.UserEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert @DynamicUpdate
public class ChatRoomUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoomEntity chatRoom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime exitDate;
    @ColumnDefault("0")
    private Boolean isExit;

    public ChatRoomUserEntity(ChatRoomEntity chatRoom, UserEntity user) {
        this.chatRoom = chatRoom;
        this.user = user;
    }

    public void enterRoom() { this.isExit = false; }
    public void exitRoom() {
        this.exitDate = LocalDateTime.now();
        this.isExit = true;
    }

}
