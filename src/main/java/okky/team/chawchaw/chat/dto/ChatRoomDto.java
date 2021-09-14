package okky.team.chawchaw.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
@AllArgsConstructor
public class ChatRoomDto {

    @NotNull
    private Long roomId;
    private String roomName;

}
