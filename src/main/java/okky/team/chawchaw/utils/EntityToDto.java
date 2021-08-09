package okky.team.chawchaw.utils;

import okky.team.chawchaw.chat.dto.ChatRoomDto;
import okky.team.chawchaw.chat.room.ChatRoomEntity;

public class EntityToDto {

    public static ChatRoomDto entityToChatRoomDto(ChatRoomEntity chatRoomEntity) {

        return new ChatRoomDto(chatRoomEntity.getId(), chatRoomEntity.getName());

    }

}
