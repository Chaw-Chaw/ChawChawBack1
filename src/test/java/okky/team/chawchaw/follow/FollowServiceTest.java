package okky.team.chawchaw.follow;

import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.user.UserService;
import okky.team.chawchaw.user.dto.RequestUserVo;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
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
        RequestUserVo requestUserVo = RequestUserVo.builder()
                .email("mangchhe@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .content("내용")
                .facebookUrl("페이스북주소")
                .instagramUrl("인스타그램주소")
                .imageUrl("이미지주소")
                .build();
        RequestUserVo requestUserVo2 = RequestUserVo.builder()
                .email("mangchhe2@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .content("내용")
                .facebookUrl("페이스북주소")
                .instagramUrl("인스타그램주소")
                .imageUrl("이미지주소")
                .build();
        userService.createUser(requestUserVo);
        userService.createUser(requestUserVo2);
        UserEntity userFrom = userRepository.findByEmail("mangchhe@naver.com").orElseGet(null);
        UserEntity userTo = userRepository.findByEmail("mangchhe2@naver.com").orElseGet(null);

        //when && then
        followService.addFollow(userFrom, userTo.getId());
        Assertions.assertThat(followRepository.findAll().size()).isEqualTo(1);
        followService.deleteFollow(userFrom, userTo.getId());
        Assertions.assertThat(followRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void 회원삭제() throws Exception {
        //given
        RequestUserVo requestUserVo = RequestUserVo.builder()
                .email("mangchhe@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .content("내용")
                .facebookUrl("페이스북주소")
                .instagramUrl("인스타그램주소")
                .imageUrl("이미지주소")
                .build();
        RequestUserVo requestUserVo2 = RequestUserVo.builder()
                .email("mangchhe2@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .content("내용")
                .facebookUrl("페이스북주소")
                .instagramUrl("인스타그램주소")
                .imageUrl("이미지주소")
                .build();
        userService.createUser(requestUserVo);
        userService.createUser(requestUserVo2);
        UserEntity userFrom = userRepository.findByEmail("mangchhe@naver.com").orElseGet(null);
        UserEntity userTo = userRepository.findByEmail("mangchhe2@naver.com").orElseGet(null);
        followService.addFollow(userFrom, userTo.getId());
        followService.addFollow(userFrom, userTo.getId());
        followService.addFollow(userFrom, userTo.getId());
        followService.addFollow(userTo, userFrom.getId());
        //when
        userService.deleteUser(userTo.getEmail());
        //then
        Assertions.assertThat(followRepository.findAll().size()).isEqualTo(0);
    }

}