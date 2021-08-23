package okky.team.chawchaw.chat.dto;

import lombok.*;
import okky.team.chawchaw.chat.MessageType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto implements Serializable {

    private MessageType messageType;
    private Long roomId;
    private Long senderId;
    private String sender;
    private String message;
    private LocalDateTime regDate;

}
