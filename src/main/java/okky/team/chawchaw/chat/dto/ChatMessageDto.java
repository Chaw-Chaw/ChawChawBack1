package okky.team.chawchaw.chat.dto;

import lombok.*;
import okky.team.chawchaw.chat.MessageType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto implements Serializable {

    @NotNull
    private MessageType messageType;
    @NotNull
    private Long roomId;
    @NotNull
    private Long senderId;
    @NotBlank
    @Length(max = 20)
    private String sender;
    @NotBlank
    @Length(max = 2000)
    private String message;
    private LocalDateTime regDate;

}
