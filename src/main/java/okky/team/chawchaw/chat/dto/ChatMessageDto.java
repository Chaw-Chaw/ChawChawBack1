package okky.team.chawchaw.chat.dto;

import lombok.*;

import java.io.Serializable;

@Getter @Setter
public class ChatMessageDto implements Serializable {

    public enum MessageType {
        ENTER, TALK, EXIT
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private String regDate;
}
