package okky.team.chawchaw.chat;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.dto.ChatRoomDto;
import okky.team.chawchaw.chat.dto.CreateChatRoomDto;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseChatMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatSubController {

    private final ChatService chatService;

    @PostMapping("/room")
    public ResponseEntity createChatRoom(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                         @RequestBody CreateChatRoomDto createChatRoomDto) {

        ChatRoomDto room = chatService.createRoom(principalDetails.getId(), createChatRoomDto.getUserId());

        return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.CREATE_ROOM_SUCCESS, true, room), HttpStatus.CREATED);
    }

    @GetMapping("/message/{userId}")
    public ResponseEntity findMessagesByUserId(@PathVariable Long userId) {
        List<ChatMessageDto> result = chatService.findMessagesByUserId(userId);

        return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.FIND_SUCCESS, true, result), HttpStatus.OK);
    }

}
