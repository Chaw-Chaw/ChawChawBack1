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
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatPubController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;
    private final ChatRoomUserService chatRoomUserService;

    @MessageMapping("/message")
    public void message(@Valid @RequestBody ChatMessageDto message) {

        if (message.getRegDate() == null) {
            message.setRegDate(LocalDateTime.now().withNano(0));
        }
        if (message.getMessageType().equals(MessageType.ENTER)) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }

        Map<Long, String> usersInRoom = chatRoomUserService.findUserIdAndEmailByChatRoomId(message.getRoomId());
        /**
         * 읽음 처리 부분
         * ws::{email} : {roomId}
         * 상대방의 유저 세션이 열려있고 메인 방에 있다면 읽음 처리
         */
        for (Map.Entry<Long, String> user : usersInRoom.entrySet()) {
            if (!user.getKey().equals(message.getSenderId()) && chatService.isConnection(user.getValue(), message.getRoomId())) {
                message.setIsRead(true);
            }
            else {
                messagingTemplate.convertAndSend("/queue/alarm/chat/" + user.getKey(), message);
                messagingTemplate.convertAndSend("/queue/chat/room/" + message.getRoomId(), message);
                message.setIsRead(false);
            }
        }
        chatService.sendMessage(message);
    }

}
