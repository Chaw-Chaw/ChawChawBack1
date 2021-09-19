package okky.team.chawchaw.chat;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.block.BlockService;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.room.ChatRoomUserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ChatPubController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;
    private final ChatRoomUserService chatRoomUserService;
    private final BlockService blockService;

    @MessageMapping("/message")
    public void message(@Valid @RequestBody ChatMessageDto message) {

        if (message.getRegDate() == null) {
            message.setRegDate(LocalDateTime.now().withNano(0));
        }
        if (message.getMessageType().equals(MessageType.ENTER)) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }

        /**
         * 읽음 처리 & 차단 여부
         * ws::{email} : {roomId}
         * 상대방의 유저 세션이 열려있고 메인 방에 있다면 읽음 처리 & 차단 여부 확인
         */
        Set<Long> blockUserIds = new HashSet<>();
        Map<Long, String> usersInRoom = chatRoomUserService.findUserIdAndEmailByChatRoomId(message.getRoomId());
        /* 나를 차단한 & 차단된 유저 구하기 */
        for (Map.Entry<Long, String> user : usersInRoom.entrySet()) {
            if (user.getKey().equals(message.getSenderId())) {
                blockUserIds.addAll(blockService.findUserId(user.getValue()));
                break;
            }
        }
        /*  */
        for (Map.Entry<Long, String> user : usersInRoom.entrySet()) {
            if (blockUserIds.contains(user.getKey()))
                continue;
            if (!user.getKey().equals(message.getSenderId()) && chatService.isConnection(user.getValue(), message.getRoomId())) {
                message.setIsRead(true);
            }
            else {
                message.setIsRead(false);
            }
            messagingTemplate.convertAndSend("/queue/chat/" + user.getKey(), message);
        }
        chatService.sendMessage(message);
    }

}
