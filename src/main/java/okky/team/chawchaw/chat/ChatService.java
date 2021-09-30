package okky.team.chawchaw.chat;

import okky.team.chawchaw.chat.dto.ChatDto;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.dto.ChatRoomDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatService {

    ChatRoomDto createRoom(Long userFromId, Long userToId);
    void deleteRoomByRoomIdAndUserId(Long roomId, Long userId);

    List<ChatDto> findMessagesByUserId(Long userId);
    List<ChatMessageDto> findMessagesByUserIdAndRegDate(Long userId);

    String uploadMessageImage(MultipartFile file);

    void sendMessage(ChatMessageDto chatMessageDto);
    Boolean updateCurrentRoom(String email, Long roomId, Long userId);
    Boolean isConnection(String email, Long roomId);
}
