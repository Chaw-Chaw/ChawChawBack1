package okky.team.chawchaw.follow;

import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        //when && then
        followService.addFollow(userFrom, userTo.getId());
        Assertions.assertThat(followRepository.findAll().size()).isEqualTo(1);
        followService.deleteFollow(userFrom, userTo.getId());
        Assertions.assertThat(followRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void 회원삭제_팔로우() throws Exception {
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
        followService.addFollow(userFrom, userTo.getId());
        followService.addFollow(userFrom, userTo.getId());
        followService.addFollow(userFrom, userTo.getId());
        followService.addFollow(userTo, userFrom.getId());
        //when
        userService.deleteUser(userTo.getId());
        //then
        Assertions.assertThat(followRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void 팔로우_상대_확인() throws Exception {
        //given
        UserEntity userFrom = userRepository.save(UserEntity.builder()
                .email("mangchhe@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .build());
        UserEntity userTo = userRepository.save(UserEntity.builder()
                .email("mangchhe1@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .build());
        UserEntity userTo2 = userRepository.save(UserEntity.builder()
                .email("mangchhe2@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .build());
        followService.addFollow(userFrom, userTo.getId());
        //when
        Boolean result = followService.isFollow(userFrom.getId(), userTo.getId());
        Boolean result2 = followService.isFollow(userFrom.getId(), userTo2.getId());
        //then
        Assertions.assertThat(result).isEqualTo(true);
        Assertions.assertThat(result2).isEqualTo(false);
    }

}