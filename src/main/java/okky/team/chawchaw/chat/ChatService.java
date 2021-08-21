package okky.team.chawchaw.chat;

import okky.team.chawchaw.chat.dto.ChatDto;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.dto.ChatRoomDto;

import java.util.List;

public interface ChatService {

    ChatMessageDto createRoom(Long userFrom, Long userTo);
    void deleteRoom(Long roomId);

    List<ChatDto> findMessagesByUserId(Long userId);

    void sendMessage(ChatMessageDto chatMessageDto);
    Boolean isRoom(Long userId, Long userId2);
}
