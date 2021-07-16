package okky.team.chawchaw.user;

import okky.team.chawchaw.follow.FollowService;
import okky.team.chawchaw.user.dto.UserDetailsDto;
import okky.team.chawchaw.user.dto.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowService followService;

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

    @Test
    public void 회원중복() throws Exception {
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
        userService.createUser(user);
        //when
        Boolean result = userService.duplicateEmail("mangchhe@naver.com");
        Boolean result2 = userService.duplicateEmail("mangchhe2@naver.com");
        //then
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(result2).isFalse();
    }

    @Test
    public void 회원삭제() throws Exception {
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
        userService.createUser(user);
        //when
        userService.deleteUser(user.getEmail());
        List<UserEntity> users = userRepository.findAll();
        //then
        Assertions.assertThat(users.size()).isEqualTo(0);
    }

    @Test
    public void 프로필_수정() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .email("mangchhe@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .content("내용")
                .country("한국")
                .language("한국어,영어")
                .hopeLanguage("중국어,독일어")
                .socialUrl("https://github.com/mangchhe")
                .imageUrl("")
                .build();
        UserDto user = userService.createUser(userDto);
        //when
        UserDto result =
                userService.updateCountry(UserDto.builder().id(user.getId()).country("미국").build());
        UserDto result2 =
                userService.updateLanguage(UserDto.builder().id(user.getId()).language("불어").build());
        UserDto result3 =
                userService.updateHopeLanguage(UserDto.builder().id(user.getId()).hopeLanguage("일본어").build());
        UserDto result4 =
                userService.updateSocialUrl(UserDto.builder().id(user.getId()).socialUrl("https://github.com/mangchhe2").build());
        UserDto result5 =
                userService.updateImageUrl(UserDto.builder().id(user.getId()).imageUrl("/src/images/avatar.jpg").build());
        UserDto result6 =
                userService.updateContent(UserDto.builder().id(user.getId()).content("내용2").build());
        //then
        Assertions.assertThat(result.getCountry()).isEqualTo("한국,미국");
        Assertions.assertThat(result2.getLanguage()).isEqualTo("한국어,영어,불어");
        Assertions.assertThat(result3.getHopeLanguage()).isEqualTo("중국어,독일어,일본어");
        Assertions.assertThat(result4.getSocialUrl()).isEqualTo("https://github.com/mangchhe,https://github.com/mangchhe2");
        Assertions.assertThat(result5.getImageUrl()).isEqualTo("/src/images/avatar.jpg");
        Assertions.assertThat(result6.getContent()).isEqualTo("내용2");
    }

    @Test
    public void 프로필_삭제() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .email("mangchhe@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .content("내용")
                .country("한국")
                .language("한국어,영어")
                .hopeLanguage("중국어,독일어")
                .socialUrl("https://github.com/mangchhe,https://github.com/mangchhe2")
                .imageUrl("/src/images/avatar.jpg")
                .build();
        UserDto user = userService.createUser(userDto);
        //when
        UserDto result =
                userService.deleteCountry(UserDto.builder().id(user.getId()).country("한국").build());
        UserDto result2 =
                userService.deleteLanguage(UserDto.builder().id(user.getId()).language("영어").build());
        UserDto result3 =
                userService.deleteHopeLanguage(UserDto.builder().id(user.getId()).hopeLanguage("중국어").build());
        UserDto result4 =
                userService.deleteSocialUrl(UserDto.builder().id(user.getId()).socialUrl("https://github.com/mangchhe2").build());
        UserDto result5 =
                userService.deleteImageUrl(UserDto.builder().id(user.getId()).imageUrl("한국").build());
        //then
        Assertions.assertThat(result.getCountry()).isEqualTo("");
        Assertions.assertThat(result2.getLanguage()).isEqualTo("한국어");
        Assertions.assertThat(result3.getHopeLanguage()).isEqualTo("독일어");
        Assertions.assertThat(result4.getSocialUrl()).isEqualTo("https://github.com/mangchhe");
        Assertions.assertThat(result5.getImageUrl()).isEqualTo("");
    }

    @Test
    public void 카드상세보기() throws Exception {
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
                .imageUrl("url")
                .build();
        UserDto user2 = UserDto.builder()
                .email("mangchhe2@naver.com")
                .password("1234")
                .name("이름2")
                .web_email("웹메일2")
                .school("학교2")
                .content("내용2")
                .country("나라2")
                .language("언어2")
                .hopeLanguage("희망언어2")
                .imageUrl("url2")
                .build();
        UserDto user3 = UserDto.builder()
                .email("mangchhe3@naver.com")
                .password("1234")
                .name("이름3")
                .web_email("웹메일3")
                .school("학교3")
                .content("내용3")
                .country("나라3")
                .language("언어3")
                .hopeLanguage("희망언어3")
                .imageUrl("url3")
                .build();
        UserDto userDto = userService.createUser(user);
        UserEntity userFrom = userRepository.findById(userService.createUser(user2).getId()).get();
        UserEntity userFrom2 = userRepository.findById(userService.createUser(user3).getId()).get();
        followService.addFollow(userFrom, userDto.getId());
        followService.addFollow(userFrom2, userDto.getId());
        //when
        UserDetailsDto result = userService.findUserDetails(userDto.getId());
        //then
        Assertions.assertThat(result.getName()).isEqualTo("이름");
        Assertions.assertThat(result.getImageUrl()).isEqualTo("url");
        Assertions.assertThat(result.getContent()).isEqualTo("내용");
        Assertions.assertThat(result.getLanguage()).isEqualTo("언어");
        Assertions.assertThat(result.getHopeLanguage()).isEqualTo("희망언어");
        Assertions.assertThat(result.getViews()).isEqualTo(0);
        Assertions.assertThat(result.getFollows()).isEqualTo(2);
    }

    @Test
    public void 프로필보기() throws Exception {
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
                .imageUrl("url")
                .build();
        UserDto user2 = UserDto.builder()
                .email("mangchhe2@naver.com")
                .password("1234")
                .name("이름2")
                .web_email("웹메일2")
                .school("학교2")
                .content("내용2")
                .country("나라2")
                .language("언어2")
                .hopeLanguage("희망언어2")
                .imageUrl("url2")
                .build();
        UserDto userDto = userService.createUser(user);
        UserDto userDto2 = userService.createUser(user2);
        //when
        UserDetailsDto result = userService.findUserProfile(userDto.getId());
        UserDetailsDto result2 = userService.findUserProfile(userDto2.getId());
        //then
        Assertions.assertThat(result.getName()).isEqualTo("이름");
        Assertions.assertThat(result.getImageUrl()).isEqualTo("url");
        Assertions.assertThat(result.getContent()).isEqualTo("내용");
        Assertions.assertThat(result.getLanguage()).isEqualTo("언어");
        Assertions.assertThat(result.getHopeLanguage()).isEqualTo("희망언어");
        Assertions.assertThat(result.getViews()).isEqualTo(0);
        Assertions.assertThat(result.getFollows()).isEqualTo(null);

        Assertions.assertThat(result2.getName()).isEqualTo("이름2");
        Assertions.assertThat(result2.getImageUrl()).isEqualTo("url2");
        Assertions.assertThat(result2.getContent()).isEqualTo("내용2");
        Assertions.assertThat(result2.getLanguage()).isEqualTo("언어2");
        Assertions.assertThat(result2.getHopeLanguage()).isEqualTo("희망언어2");
        Assertions.assertThat(result2.getViews()).isEqualTo(0);
        Assertions.assertThat(result2.getFollows()).isEqualTo(null);
    }

}