package okky.team.chawchaw.chat;

import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.dto.ChatRoomDto;

import java.util.List;

public interface ChatService {

    ChatRoomDto createRoom(Long userFrom, Long userTo);

    List<ChatMessageDto> findMessagesByUserId(Long userId);

    void sendMessage(ChatMessageDto chatMessageDto);
}
