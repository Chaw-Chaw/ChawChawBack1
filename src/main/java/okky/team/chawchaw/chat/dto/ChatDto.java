package okky.team.chawchaw.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ChatDto {

    private Long roodId;
    private String sender;
    private String imageUrl;
    private List<ChatMessageDto> messages;

    public ChatDto(Long roodId, String sender, String imageUrl, List<ChatMessageDto> messages) {
        this.roodId = roodId;
        this.sender = sender;
        this.imageUrl = imageUrl;
        this.messages = messages;
    }
}
