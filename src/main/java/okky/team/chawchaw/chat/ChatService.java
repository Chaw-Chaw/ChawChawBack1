package okky.team.chawchaw.chat;

import okky.team.chawchaw.chat.dto.ChatDto;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.dto.ChatRoomDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatService {

    ChatMessageDto createRoom(Long userFrom, Long userTo);
    void deleteRoom(Long roomId);

    List<ChatDto> findMessagesByUserId(Long userId);

    String uploadMessageImage(MultipartFile file);

    void sendMessage(ChatMessageDto chatMessageDto);
    Boolean isRoom(Long userId, Long userId2);
}
