package okky.team.chawchaw.chat.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto implements Serializable {

    private Long roomId;
    private Long senderId;
    private String sender;
    private String message;
    private LocalDateTime regDate;

}
