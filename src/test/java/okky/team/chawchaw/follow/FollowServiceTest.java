package okky.team.chawchaw.follow;

import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.user.UserService;
import okky.team.chawchaw.user.dto.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FollowServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowService followService;
    @Autowired
    private FollowRepository followRepository;

    @Test
    public void 팔로우_언팔로우() throws Exception {
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
        UserDto user2 = UserDto.builder()
                .email("mangchhe2@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .content("내용")
                .country("나라")
                .language("언어")
                .hopeLanguage("희망언어")
                .build();
        UserEntity userFrom = userRepository.findById(userService.createUser(user).getId()).get();
        Long userTo = userService.createUser(user2).getId();
        //when && then
        followService.addFollow(userFrom, userTo);
        Assertions.assertThat(followRepository.findAll().size()).isEqualTo(1);
        followService.deleteFollow(userFrom, userTo);
        Assertions.assertThat(followRepository.findAll().size()).isEqualTo(0);
    }

}