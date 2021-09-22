package okky.team.chawchaw.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.like.dto.LikeMessageDto;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDto {

    List<ChatMessageDto> messages;
    List<LikeMessageDto> likes;

}
