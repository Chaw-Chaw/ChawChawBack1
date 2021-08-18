package okky.team.chawchaw.chat;

import okky.team.chawchaw.chat.dto.ChatDto;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.chat.room.ChatRoomEntity;
import okky.team.chawchaw.chat.room.ChatRoomRepository;
import okky.team.chawchaw.chat.room.ChatRoomUserEntity;
import okky.team.chawchaw.chat.room.ChatRoomUserRepository;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
class ChatServiceImplTest {

    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatRoomUserRepository chatRoomUserRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void 방생성() throws Exception {
        //given
        UserEntity userFrom = userRepository.save(UserEntity.builder()
                .email("mangchhe@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .build());
        UserEntity userTo = userRepository.save(UserEntity.builder()
                .email("mangchhe2@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .build());
        //when
        chatService.createRoom(userFrom.getId(), userTo.getId());
        //then
        List<ChatRoomEntity> rooms = chatRoomRepository.findAll();
        List<ChatRoomUserEntity> roomUsers = chatRoomUserRepository.findAll();
        List<ChatMessageDto> messages = chatMessageRepository.findAllByRoomId(rooms.get(0).getId());
        Assertions.assertThat(rooms.size()).isEqualTo(1);
        Assertions.assertThat(roomUsers.size()).isEqualTo(2);
        Assertions.assertThat(messages.get(0)).extracting("roomId", "sender", "message").isEqualTo(
                Arrays.asList(rooms.get(0).getId(), "이름", "이름님이 입장하셨습니다.")
        );
    }

    @Test
    public void 메시지_보내기() throws Exception {
        //when
        chatService.sendMessage(new ChatMessageDto(
                1L, 1L, "테스터", "안녕하세요.", LocalDateTime.now()
        ));
        Set<String> keys = redisTemplate.keys("*");
        List<ChatMessageDto> result = keys.stream().map(x -> (ChatMessageDto) redisTemplate.opsForValue().get(x)).collect(Collectors.toList());
        //then
        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0))
                .extracting("roomId", "senderId", "sender", "message")
                .contains(1L, 1L, "테스터", "안녕하세요.");
    }

    @Test
    public void 해당아이디_메시지가져오기() throws Exception {
        //given
        UserEntity userFrom = userRepository.save(UserEntity.builder()
                .email("mangchhe@naver.com")
                .password("1234")
                .name("테스트1")
                .web_email("웹메일")
                .school("학교")
                .build());
        UserEntity userTo = userRepository.save(UserEntity.builder()
                .email("mangchhe2@naver.com")
                .password("1234")
                .name("테스트2")
                .web_email("웹메일")
                .school("학교")
                .build());
        ChatMessageDto room = chatService.createRoom(userFrom.getId(), userTo.getId());
        chatService.sendMessage(new ChatMessageDto(
                room.getRoomId(), userTo.getId(), userTo.getName(), "마이크오바1", LocalDateTime.now()
        ));
        //when
        List<ChatDto> result = chatService.findMessagesByUserId(userFrom.getId());
        //then
        for (ChatDto chatDto : result) {
            Assertions.assertThat(chatDto)
                    .extracting("roomId", "senderId", "sender", "imageUrl", "messages")
                    .contains(
                            room.getRoomId(),
                            userTo.getId(),
                            userTo.getName(),
                            userTo.getImageUrl()
                    );
            Assertions.assertThat(chatDto.getMessages().get(0))
                    .extracting("roomId", "senderId", "sender", "message")
                    .contains(
                            room.getRoomId(),
                            userTo.getId(),
                            userTo.getName(),
                            "마이크오바1"
                    );
            Assertions.assertThat(chatDto.getMessages().get(1))
                    .extracting("roomId", "senderId", "sender", "message")
                    .contains(
                            room.getRoomId(),
                            userFrom.getId(),
                            userFrom.getName(),
                            userFrom.getName() + "님이 입장하셨습니다."
                    );
        }
    }

}