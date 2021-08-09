package okky.team.chawchaw.chat;

import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.dto.ChatRoomDto;

public interface ChatService {

    ChatRoomDto createRoom(Long userFrom, Long userTo);

    void sendMessage(ChatMessageDto chatMessageDto);
}
