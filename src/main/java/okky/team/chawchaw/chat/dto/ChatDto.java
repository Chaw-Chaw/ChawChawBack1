package okky.team.chawchaw.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ChatDto {

    private Long roomId;
    private List<String> participantNames;
    private List<Long> participantIds;
    private List<String> participantImageUrls;
    private List<ChatMessageDto> messages;

    public ChatDto(Long roomId, List<String> participantNames, List<Long> participantIds, List<String> participantImageUrls, List<ChatMessageDto> messages) {
        this.roomId = roomId;
        this.participantNames = participantNames;
        this.participantIds = participantIds;
        this.participantImageUrls = participantImageUrls;
        this.messages = messages;
    }
}
