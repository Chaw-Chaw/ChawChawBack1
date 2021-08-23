package okky.team.chawchaw.chat;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.chat.dto.ChatDto;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.dto.ChatRoomDto;
import okky.team.chawchaw.chat.room.ChatRoomEntity;
import okky.team.chawchaw.chat.room.ChatRoomRepository;
import okky.team.chawchaw.chat.room.ChatRoomUserEntity;
import okky.team.chawchaw.chat.room.ChatRoomUserRepository;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.utils.EntityToDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = false)
    public ChatMessageDto createRoom(Long userFrom, Long userTo) {
        ChatRoomEntity room = chatRoomRepository.save(new ChatRoomEntity(UUID.randomUUID().toString()));
        UserEntity user = userRepository.findById(userFrom).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        UserEntity user2 = userRepository.findById(userTo).orElseThrow(() -> new UsernameNotFoundException("not found user"));
        chatRoomUserRepository.save(new ChatRoomUserEntity(room, user));
        chatRoomUserRepository.save(new ChatRoomUserEntity(room, user2));
        ChatMessageDto message = new ChatMessageDto(room.getId(), user.getId(), user.getName(), user.getName() + "님이 입장하셨습니다.", LocalDateTime.now().withNano(0));
        chatMessageRepository.save(message);
        return message;
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteRoom(Long roomId) {
        chatRoomRepository.deleteById(roomId);
        chatMessageRepository.deleteByRoomId(roomId);
    }

    @Override
    @Transactional(readOnly = false)
    public List<ChatDto> findMessagesByUserId(Long userId) {
        List<ChatDto> result = new ArrayList<>();
        List<ChatRoomUserEntity> users = chatRoomUserRepository.findAllByUserId(userId);
        for (ChatRoomUserEntity user : users) {
            for (ChatRoomUserEntity roomUsers : chatRoomUserRepository.findAllByChatRoomId(user.getChatRoom().getId())) {
                if (!roomUsers.getUser().getId().equals(userId)) {
                    result.add(new ChatDto(
                            user.getChatRoom().getId(),
                            roomUsers.getUser().getId(),
                            roomUsers.getUser().getName(),
                            roomUsers.getUser().getImageUrl(),
                            chatMessageRepository.findAllByRoomIdOrderByRegDateAsc(user.getChatRoom().getId())
                    ));
                }
            }
        }
        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public void sendMessage(ChatMessageDto chatMessageDto) {
        chatMessageRepository.save(chatMessageDto);
    }

    @Override

    public Boolean isRoom(Long userId, Long userId2) {
        Boolean result = chatRoomUserRepository.isChatRoom(userId, userId2);
        return result != null && result;
    }
}
