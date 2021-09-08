package okky.team.chawchaw.chat;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.room.ChatRoomUserService;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatPubController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;
    private final ChatRoomUserService chatRoomUserService;

    @MessageMapping("/message")
    public void message(@Valid @RequestBody ChatMessageDto message) {
//        PrincipalDetails principal = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(principal.getId());
//        System.out.println(principal.getSchool());
        if (message.getRegDate() == null) {
            message.setRegDate(LocalDateTime.now().withNano(0));
        }
        if (message.getMessageType().equals(MessageType.ENTER)) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        chatService.sendMessage(message);
        messagingTemplate.convertAndSend("/queue/chat/room/" + message.getRoomId(), message);
        for (Long userId : chatRoomUserService.findUserIdsByChatRoomId(message.getRoomId())) {
            if (!message.getSenderId().equals(userId)) {
                messagingTemplate.convertAndSend("/queue/alarm/chat/" + userId, message);
            }
        }

    }

}
