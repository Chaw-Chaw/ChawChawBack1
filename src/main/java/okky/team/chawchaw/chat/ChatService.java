package okky.team.chawchaw.chat;

import okky.team.chawchaw.chat.dto.ChatDto;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatService {

    ChatMessageDto createRoom(Long userFrom, Long userTo);
    void deleteRoom(Long roomId);

    List<ChatDto> findMessagesByUserId(Long userId);
    List<ChatMessageDto> findMessagesByUserIdAndRegDate(Long userId);

    String uploadMessageImage(MultipartFile file);

    void sendMessage(ChatMessageDto chatMessageDto);
    Boolean updateCurrentRoom(String email, Long roomId, Long userId);
    Boolean isRoom(Long userId, Long userId2);
    Boolean isConnection(String email, Long roomId);
}
