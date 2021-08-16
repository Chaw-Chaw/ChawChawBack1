package okky.team.chawchaw.chat;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.chat.dto.ChatDto;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.dto.ChatRoomDto;
import okky.team.chawchaw.chat.dto.CreateChatRoomDto;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseChatMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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


        List<ChatDto> result = null;
        Boolean isRoom = chatService.isRoom(principalDetails.getId(), createChatRoomDto.getUserId());
        Boolean isRoom2 = chatService.isRoom(createChatRoomDto.getUserId(), principalDetails.getId());

        if (isRoom || isRoom2) {
            chatService.createRoom(principalDetails.getId(), createChatRoomDto.getUserId());
            result = chatService.findMessagesByUserId(principalDetails.getId());
            return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.CREATE_ROOM_SUCCESS, true, result), HttpStatus.CREATED);
        }

        result = chatService.findMessagesByUserId(principalDetails.getId());

        return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.EXIST_ROOM, true, result), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity findMessagesByUserId(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<ChatDto> result = chatService.findMessagesByUserId(principalDetails.getId());

        return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.FIND_SUCCESS, true, result), HttpStatus.OK);
    }

}
