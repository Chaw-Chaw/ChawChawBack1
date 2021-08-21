package okky.team.chawchaw.chat;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.chat.dto.*;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.exception.PointMyselfException;
import okky.team.chawchaw.utils.message.ResponseChatMessage;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatSubController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ModelMapper mapper;

    @PostMapping("/room")
    public ResponseEntity createChatRoom(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                         @RequestBody CreateChatRoomDto createChatRoomDto) {

        if (principalDetails.getId().equals(createChatRoomDto.getUserId()))
            throw new PointMyselfException();

        List<ChatDto> result = null;
        Boolean isRoom = chatService.isRoom(principalDetails.getId(), createChatRoomDto.getUserId());

        if (isRoom) {
            result = chatService.findMessagesByUserId(principalDetails.getId());
            return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.EXIST_ROOM, true, result), HttpStatus.OK);
        }

        ChatMessageDto message = chatService.createRoom(principalDetails.getId(), createChatRoomDto.getUserId());
        ChatStatusMessageDto statusMessage = mapper.map(message, ChatStatusMessageDto.class);
        statusMessage.setMessageType(MessageType.ENTER);

        result = chatService.findMessagesByUserId(principalDetails.getId());

        messagingTemplate.convertAndSend("/queue/chat/room/wait/" + createChatRoomDto.getUserId(), statusMessage);

        return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.CREATE_ROOM_SUCCESS, true, result), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity findMessagesByUserId(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<ChatDto> result = chatService.findMessagesByUserId(principalDetails.getId());

        return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.FIND_SUCCESS, true, result), HttpStatus.OK);
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity deleteChatRoom(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                         @PathVariable Long roomId) {
        try {
            chatService.deleteRoom(roomId);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.NOT_EXIST_ROOM, false), HttpStatus.OK);
        }
        ChatStatusMessageDto statusMessage = new ChatStatusMessageDto(
                MessageType.EXIT, roomId, principalDetails.getId(), principalDetails.getName(),
                principalDetails.getName() + "님이 퇴장하셨습니다.", LocalDateTime.now().withNano(0));

        messagingTemplate.convertAndSend("/queue/chat/room/" + roomId, statusMessage);

        return new ResponseEntity(DefaultResponseVo.res(ResponseChatMessage.DELETE_SUCCESS, true), HttpStatus.OK);
    }

}
