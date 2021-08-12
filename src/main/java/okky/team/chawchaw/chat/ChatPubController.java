package okky.team.chawchaw.chat;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatPubController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/message")
    public void message(@RequestBody ChatMessageDto message) {
        if (message.getRegDate() == null) {
            message.setRegDate(LocalDateTime.now());
        }
        chatService.sendMessage(message);
        messagingTemplate.convertAndSend("/queue/chat/room/" + message.getRoomId(), message);

    }

}
