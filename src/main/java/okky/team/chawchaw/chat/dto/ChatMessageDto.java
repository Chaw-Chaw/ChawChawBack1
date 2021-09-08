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
    @NotBlank
    private String imageUrl;
    private LocalDateTime regDate;
    private Boolean isRead;

    public ChatMessageDto(@NotNull MessageType messageType, @NotNull Long roomId, @NotNull Long senderId, @NotBlank @Length(max = 20) String sender, @NotBlank @Length(max = 2000) String message, @NotBlank String imageUrl, LocalDateTime regDate) {
        this.messageType = messageType;
        this.roomId = roomId;
        this.senderId = senderId;
        this.sender = sender;
        this.message = message;
        this.imageUrl = imageUrl;
        this.regDate = regDate;
    }
}
