package okky.team.chawchaw.chat.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class DeleteChatRoomDto {

    @NotNull
    private Long roomId;

}
