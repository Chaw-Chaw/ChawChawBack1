package okky.team.chawchaw.chat;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ChatPubController {

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/message")
    public void message(@RequestBody ChatMessageDto message) {

        messagingTemplate.convertAndSend("/queue/chat/room/" + message.getRoomId(), message);

    }

}
