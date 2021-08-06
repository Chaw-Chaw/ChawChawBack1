package okky.team.chawchaw.chat;

import okky.team.chawchaw.chat.dto.ChatRoomDto;

public interface ChatService {

    public ChatRoomDto createRoom(Long userFrom, Long userTo);
}
