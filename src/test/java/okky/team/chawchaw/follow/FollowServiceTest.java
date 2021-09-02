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
    public void 팔로우_했던_상대들_인지_확인() throws Exception {
        //given
        List<Long> userIds = new ArrayList<>();
        UserEntity userFrom = userRepository.save(UserEntity.builder()
                .email("mangchhe@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .build());
        for (int i = 0; i < 3; i++) {
            UserEntity userTo = userRepository.save(UserEntity.builder()
                    .email("mangchhe" + i + "@naver.com")
                    .password("1234")
                    .name("이름")
                    .web_email("웹메일")
                    .school("학교")
                    .build());
            userIds.add(userTo.getId());
            if (i < 2)
                followService.addFollow(userFrom, userTo.getId());
        }
        //when
        List<Long> followTos = followService.isFollowTos(userFrom.getId(), userIds);
        //then
        Assertions.assertThat(followTos).containsOnly(userIds.get(0), userIds.get(1));
    }

}