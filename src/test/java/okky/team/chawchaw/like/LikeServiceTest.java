package okky.team.chawchaw.like;

import okky.team.chawchaw.like.dto.CreateLikeDto;
import okky.team.chawchaw.like.dto.DeleteLikeDto;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.user.UserService;
import okky.team.chawchaw.user.dto.DeleteUserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
class LikeServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikeService likeService;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Test
    public void 좋아요() throws Exception {
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
        likeService.addLike(new CreateLikeDto(userFrom.getId(), userFrom.getName(), userTo.getId()));
        Assertions.assertThat(likeRepository.findAll().size()).isEqualTo(1);
        likeService.deleteLike(new DeleteLikeDto(userFrom.getId(), userFrom.getName(), userTo.getId()));
        Assertions.assertThat(likeRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void 회원삭제_좋아요() throws Exception {
        //given
        UserEntity userFrom = userRepository.save(UserEntity.builder()
                .email("mangchhe@naver.com")
                .password(passwordEncoder.encode("1234"))
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .build());
        UserEntity userTo = userRepository.save(UserEntity.builder()
                .email("mangchhe2@naver.com")
                .password(passwordEncoder.encode("1234"))
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .build());
        likeService.addLike(new CreateLikeDto(userFrom.getId(), userFrom.getName(), userTo.getId()));
        likeService.addLike(new CreateLikeDto(userTo.getId(), userFrom.getName(), userFrom.getId()));
        //when
        userService.deleteUser(userTo.getId());
        //then
        Assertions.assertThat(likeRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void 좋아요_상대_확인() throws Exception {
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
        likeService.addLike(new CreateLikeDto(userFrom.getId(), userFrom.getName(), userTo.getId()));
        //when
        Boolean result = likeService.isLike(userFrom.getId(), userTo.getId());
        Boolean result2 = likeService.isLike(userFrom.getId(), userTo2.getId());
        //then
        Assertions.assertThat(result).isEqualTo(true);
        Assertions.assertThat(result2).isEqualTo(false);
    }

}