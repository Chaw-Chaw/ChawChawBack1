package okky.team.chawchaw.chat.room;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import okky.team.chawchaw.user.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "chat_room_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_from")
    private UserEntity userFrom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_to")
    private UserEntity userTo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoomEntity chatRoom;

    public ChatRoomUserEntity(UserEntity userFrom, UserEntity userTo, ChatRoomEntity chatRoom) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.chatRoom = chatRoom;
    }
}
