package okky.team.chawchaw.chat.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class ChatMessageDto implements Serializable {

    private Long roomId;
    private String sender;
    private String message;
    private LocalDateTime regDate;

}
