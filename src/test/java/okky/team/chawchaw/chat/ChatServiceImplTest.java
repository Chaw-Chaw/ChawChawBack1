package okky.team.chawchaw.chat;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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
        List<ChatMessageDto> messages = chatMessageRepository.findAll(rooms.get(0).getId());
        Assertions.assertThat(rooms.size()).isEqualTo(1);
        Assertions.assertThat(roomUsers.size()).isEqualTo(2);
        Assertions.assertThat(messages.get(0)).extracting("roomId", "sender", "message").isEqualTo(
                Arrays.asList(rooms.get(0).getId(), "이름", "이름님이 입장하셨습니다.")
        );
    }

}