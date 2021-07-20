package okky.team.chawchaw.follow;

import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.user.UserService;
import okky.team.chawchaw.user.dto.CreateUserVo;
import okky.team.chawchaw.user.dto.RequestUserVo;
import okky.team.chawchaw.user.dto.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
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
        CreateUserVo createUserVo = CreateUserVo.builder()
                .email("mangchhe@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .content("내용")
                .facebookUrl("페이스북주소")
                .instagramUrl("인스타그램주소")
                .imageUrl("이미지주소")
                .language(Arrays.asList(
                        "kv", "ko"
                ))
                .hopeLanguage(Arrays.asList(
                        "en", "ko"
                ))
                .country(Arrays.asList(
                        "United States", "South Korea"
                ))
                .build();
        CreateUserVo createUserVo2 = CreateUserVo.builder()
                .email("mangchhe2@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .content("내용")
                .facebookUrl("페이스북주소")
                .instagramUrl("인스타그램주소")
                .imageUrl("이미지주소")
                .language(Arrays.asList(
                        "kv", "ko"
                ))
                .hopeLanguage(Arrays.asList(
                        "en", "ko"
                ))
                .country(Arrays.asList(
                        "United States", "South Korea"
                ))
                .build();
        userService.createUser(createUserVo);
        userService.createUser(createUserVo2);
        UserEntity userFrom = userRepository.findByEmail("mangchhe@naver.com").get(0);
        UserEntity userTo = userRepository.findByEmail("mangchhe2@naver.com").get(0);

        //when && then
        followService.addFollow(userFrom, userTo.getId());
        Assertions.assertThat(followRepository.findAll().size()).isEqualTo(1);
        followService.deleteFollow(userFrom, userTo.getId());
        Assertions.assertThat(followRepository.findAll().size()).isEqualTo(0);
    }

}