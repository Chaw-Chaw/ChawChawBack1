package okky.team.chawchaw.chat;

import okky.team.chawchaw.chat.dto.ChatMessageDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
class ChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Test
    public void 삽입() throws Exception {
        //given
        Long uuid = 1L;
        for (int i = 0; i < 10; i++) {
            chatMessageRepository.save(new ChatMessageDto(
                    uuid,
                    "하주현" + String.valueOf(i),
                    "안녕" + String.valueOf(i),
                    LocalDateTime.now()
            ));
        }
        //when
        System.out.println("무작위");
        System.out.println(chatMessageRepository.findAll(uuid).size());
        chatMessageRepository.findAll(uuid).forEach(x -> System.out.println(x.toString()));

        chatMessageRepository.remove(uuid);

        System.out.println("순서대로");
        System.out.println(chatMessageRepository.findAllOrderByRegDateDesc(uuid).size());
        chatMessageRepository.findAllOrderByRegDateDesc(uuid).forEach(x -> System.out.println(x.toString()));
        //then
    }

}