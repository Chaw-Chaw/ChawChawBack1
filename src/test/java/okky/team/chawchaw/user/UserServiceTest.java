package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        UserDto user = UserDto.builder()
                .email("mangchhe@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .content("내용")
                .country("나라")
                .language("언어")
                .hopeLanguage("희망언어")
                .build();
        //when
        userService.createUser(user);
        List<UserEntity> users = userRepository.findAll();
        //then
        Assertions.assertThat(users.size()).isEqualTo(1);
        Assertions.assertThat(users.get(0).getEmail())
                .isEqualTo("mangchhe@naver.com");
    }

}