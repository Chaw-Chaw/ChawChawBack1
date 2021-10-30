package okky.team.chawchaw.chat;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.block.BlockService;
import okky.team.chawchaw.chat.dto.*;
import okky.team.chawchaw.chat.room.ChatRoomUserService;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.user.UserService;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.chat.exception.NotExistRoomException;
import okky.team.chawchaw.utils.message.ResponseChatMessage;
import okky.team.chawchaw.utils.message.ResponseGlobalMessage;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatSubController {

    private final ChatService chatService;
    private final ChatRoomUserService chatRoomUserService;
    private final BlockService blockService;
    private final UserService userService;

    @PostMapping("/room")
    public ResponseEntity<?> createChatRoom(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                         @Valid @RequestBody CreateChatRoomDto createChatRoomDto) {

        userService.validMyself(principalDetails.getId(), createChatRoomDto.getUserId());
        blockService.validBlockUser(principalDetails.getId(), createChatRoomDto.getUserId());

        Long roomId = chatRoomUserService.findRoomIdByUserIds(principalDetails.getId(), createChatRoomDto.getUserId());

        if (roomId != -1) {
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseChatMessage.C401), HttpStatus.BAD_REQUEST);
        }

        ChatRoomDto result = chatService.createRoom(principalDetails.getId(), createChatRoomDto.getUserId());

        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200, result), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> findRoomMessages(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        List<ChatDto> result = chatService.findMessagesByUserId(principalDetails.getId());

        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200, result), HttpStatus.OK);
    }

    @GetMapping("/room/{userId}")
    public ResponseEntity<?> findRoom(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                      @PathVariable Long userId) {

        Long roomId = chatRoomUserService.findRoomIdByUserIds(principalDetails.getId(), userId);

        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200, new ChatRoomDto(roomId, "none")), HttpStatus.OK);
    }

    @PostMapping("/room/enter")
    public ResponseEntity<?> updateCurrentRoom(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @Valid @RequestBody ChatRoomDto chatRoomDto) {

        Boolean result = chatService.updateCurrentRoom(principalDetails.getUsername(), chatRoomDto.getRoomId(), principalDetails.getId());
        if (result)
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200), HttpStatus.OK);
        else
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseChatMessage.C400), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/room")
    public ResponseEntity<?> deleteChatRoom(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                         @Valid @RequestBody DeleteChatRoomDto deleteChatRoomDto) {
        try {
            chatService.deleteRoomByRoomIdAndUserId(deleteChatRoomDto.getRoomId(), principalDetails.getId());

        }  catch (NotExistRoomException | EmptyResultDataAccessException e) {
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseChatMessage.C402), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200), HttpStatus.OK);
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadChatImage(@RequestParam MultipartFile file) {

        String result = chatService.uploadMessageImage(file);

        if (!result.isEmpty())
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200, result), HttpStatus.OK);
        else
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseChatMessage.C403), HttpStatus.BAD_REQUEST);
    }

}
