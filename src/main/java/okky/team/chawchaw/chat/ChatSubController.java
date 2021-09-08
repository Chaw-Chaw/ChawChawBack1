package okky.team.chawchaw.chat;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.chat.dto.*;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.exception.PointMyselfException;
import okky.team.chawchaw.utils.message.ResponseChatMessage;
import okky.team.chawchaw.utils.message.ResponseFileMessage;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatSubController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations messagingTemplate;

    @PostMapping("/room")
    public ResponseEntity createChatRoom(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                         @Valid @RequestBody CreateChatRoomDto createChatRoomDto) {

        if (principalDetails.getId().equals(createChatRoomDto.getUserId()))
            throw new PointMyselfException();

        List<ChatDto> result = null;
        Boolean isRoom = chatService.isRoom(principalDetails.getId(), createChatRoomDto.getUserId());

        if (isRoom) {
            result = chatService.findMessagesByUserId(principalDetails.getId());
            return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.EXIST_ROOM, true, result), HttpStatus.OK);
        }

        ChatMessageDto message = chatService.createRoom(principalDetails.getId(), createChatRoomDto.getUserId());

        result = chatService.findMessagesByUserId(principalDetails.getId());

        messagingTemplate.convertAndSend("/queue/chat/room/wait/" + createChatRoomDto.getUserId(), message);
        messagingTemplate.convertAndSend("/queue/alarm/chat/" + createChatRoomDto.getUserId(), message);

        return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.CREATE_ROOM_SUCCESS, true, result), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity findMessagesByUserId(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<ChatDto> result = chatService.findMessagesByUserId(principalDetails.getId());

        return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.FIND_SUCCESS, true, result), HttpStatus.OK);
    }

    @PostMapping("/room/enter")
    public ResponseEntity updateCurrentRoom(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @RequestBody ChatRoomDto chatRoomDto) {

        Boolean result = chatService.updateCurrentRoom(principalDetails.getUsername(), chatRoomDto.getRoomId(), principalDetails.getId());
        if (result)
            return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.MOVE_ROOM_SUCCESS, true), HttpStatus.OK);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.MOVE_ROOM_FAIL, false), HttpStatus.OK);
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity deleteChatRoom(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                         @PathVariable Long roomId) {
        try {
            chatService.deleteRoom(roomId);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.NOT_EXIST_ROOM, false), HttpStatus.OK);
        }
        ChatMessageDto message = new ChatMessageDto(
                MessageType.EXIT, roomId, principalDetails.getId(), principalDetails.getName(),
                principalDetails.getName() + "님이 퇴장하셨습니다.", principalDetails.getImageUrl(), LocalDateTime.now().withNano(0));

        messagingTemplate.convertAndSend("/queue/chat/room/" + roomId, message);

        return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.DELETE_SUCCESS, true), HttpStatus.OK);
    }

    @PostMapping("/image")
    public ResponseEntity uploadChatImage(@RequestParam MultipartFile file) {

        String result = chatService.uploadMessageImage(file);

        if (!result.isEmpty())
            return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.UPLOAD_SUCCESS, true, result), HttpStatus.OK);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.UPLOAD_FAIL, false), HttpStatus.OK);
    }

}
