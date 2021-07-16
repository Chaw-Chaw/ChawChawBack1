package okky.team.chawchaw.user;

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

}