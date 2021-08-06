package okky.team.chawchaw.chat;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.dto.ChatRoomDto;
import okky.team.chawchaw.chat.room.ChatRoomEntity;
import okky.team.chawchaw.chat.room.ChatRoomRepository;
import okky.team.chawchaw.chat.room.ChatRoomUserEntity;
import okky.team.chawchaw.chat.room.ChatRoomUserRepository;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.utils.EntityToDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public ChatRoomDto createRoom(Long userFrom, Long userTo) {
        ChatRoomEntity room = chatRoomRepository.save(new ChatRoomEntity(UUID.randomUUID().toString()));
        UserEntity user = userRepository.findById(userTo).orElseThrow();
        UserEntity user2 = userRepository.findById(userTo).orElseThrow();
        chatRoomUserRepository.save(new ChatRoomUserEntity(user, room));
        chatRoomUserRepository.save(new ChatRoomUserEntity(user2, room));
        chatMessageRepository.save(new ChatMessageDto(room.getId(), user.getName(), user.getName() + "님이 입장하셨습니다.", LocalDateTime.now()));
        return EntityToDto.entityToChatRoomDto(room);
    }
}
