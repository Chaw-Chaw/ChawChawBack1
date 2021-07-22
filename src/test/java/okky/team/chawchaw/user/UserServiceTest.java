package okky.team.chawchaw.user;

import okky.team.chawchaw.follow.FollowService;
import okky.team.chawchaw.user.country.UserCountryRepository;
import okky.team.chawchaw.user.dto.RequestUserVo;
import okky.team.chawchaw.user.dto.UserDetailsDto;
import okky.team.chawchaw.user.dto.UserProfileDto;
import okky.team.chawchaw.user.language.UserHopeLanguageRepository;
import okky.team.chawchaw.user.language.UserLanguageRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowService followService;
    @Autowired
    private UserCountryRepository userCountryRepository;
    @Autowired
    private UserLanguageRepository userLanguageRepository;
    @Autowired
    private UserHopeLanguageRepository userHopeLanguageRepository;

    @Test
    public void 회원가입() throws Exception {
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
                .imageUrl("이미지 주소")
                .language(Sets.newHashSet(Arrays.asList(
                        "ko",
                        "kv",
                        "kg"
                )))
                .hopeLanguage(Sets.newHashSet(Arrays.asList(
                        "en",
                        "ee"
                )))
                .country(Sets.newHashSet(Arrays.asList(
                        "South Korea",
                        "United States"
                )))
                .build();
        //when
        userService.createUser(requestUserVo);
        List<UserEntity> users = userRepository.findAll();
        List<String> countrys = userCountryRepository.findAll().stream().map(x -> x.getCountry().getName()).collect(Collectors.toList());
        List<String> languages = userLanguageRepository.findAll().stream().map(x -> x.getLanguage().getAbbr()).collect(Collectors.toList());
        List<String> hopeLanguages = userHopeLanguageRepository.findAll().stream().map(x -> x.getHopeLanguage().getAbbr()).collect(Collectors.toList());
        //then
        // 유저
        Assertions.assertThat(users.size()).isEqualTo(1);
        Assertions.assertThat(users.get(0).getEmail())
                .isEqualTo("mangchhe@naver.com");
        // 나라
        Assertions.assertThat(countrys.size()).isEqualTo(2);
        Assertions.assertThat(countrys)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("South Korea", "United States"));
        // 언어
        Assertions.assertThat(languages.size()).isEqualTo(3);
        Assertions.assertThat(languages)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("ko", "kv", "kg"));
        // 희망 언어
        Assertions.assertThat(hopeLanguages.size()).isEqualTo(2);
        Assertions.assertThat(hopeLanguages)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("en", "ee"));
    }

    @Test
    public void 회원중복() throws Exception {
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
                .imageUrl("이미지 주소")
                .language(Sets.newHashSet(Arrays.asList(
                        "ko",
                        "kv",
                        "kg"
                )))
                .hopeLanguage(Sets.newHashSet(Arrays.asList(
                        "en",
                        "ee"
                )))
                .country(Sets.newHashSet(Arrays.asList(
                        "South Korea",
                        "United States"
                )))
                .build();
        userService.createUser(requestUserVo);
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
        RequestUserVo requestUserVo = RequestUserVo.builder()
                .email("mangchhe@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .content("내용")
                .facebookUrl("페이스북주소")
                .instagramUrl("인스타그램주소")
                .imageUrl("이미지 주소")
                .language(Sets.newHashSet(Arrays.asList(
                        "ko",
                        "kv",
                        "kg"
                )))
                .hopeLanguage(Sets.newHashSet(Arrays.asList(
                        "en",
                        "ee"
                )))
                .country(Sets.newHashSet(Arrays.asList(
                        "South Korea",
                        "United States"
                )))
                .build();

        userService.createUser(requestUserVo);
        //when
        userService.deleteUser(requestUserVo.getEmail());
        List<UserEntity> users = userRepository.findAll();
        //then
        Assertions.assertThat(users.size()).isEqualTo(0);
    }

    @Test
    public void 프로필_수정() throws Exception {
        //given
        RequestUserVo createVo = RequestUserVo.builder()
                .email("mangchhe@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .content("내용")
                .facebookUrl("페이스북주소")
                .instagramUrl("인스타그램주소")
                .imageUrl("이미지주소")
                .language(Sets.newHashSet(Arrays.asList(
                        "fy",
                        "xh",
                        "yi",
                        "yo"
                )))
                .hopeLanguage(Sets.newHashSet(Arrays.asList(
                        "ab",
                        "aa",
                        "af",
                        "ak"
                )))
                .country(Sets.newHashSet(Arrays.asList(
                        "United States",
                        "South Korea",
                        "Zambia",
                        "Zimbabwe"
                )))
                .build();
        userService.createUser(createVo);
        UserEntity user = userRepository.findAll().get(0);
        RequestUserVo requestUserVo = RequestUserVo.builder()
                .id(user.getId())
                .content("내용2")
                .imageUrl("이미지주소2")
                .facebookUrl("페이스북주소2")
                .instagramUrl("인스타그램주소2")
                .country(Sets.newHashSet(Arrays.asList(
                        "United States",
                        "South Korea",
                        "Samoa",
                        "Kosovo"
                )))
                .language(Sets.newHashSet(Arrays.asList(
                        "fy",
                        "xh",
                        "wo",
                        "cy"
                )))
                .hopeLanguage(Sets.newHashSet(Arrays.asList(
                        "ab",
                        "aa",
                        "sq",
                        "am"
                )))
                .build();

        //when
        userService.updateProfile(requestUserVo);

        //then
        List<String> countrys = userCountryRepository.findAll().stream().map(x -> x.getCountry().getName()).collect(Collectors.toList());
        List<String> languages = userLanguageRepository.findAll().stream().map(x -> x.getLanguage().getAbbr()).collect(Collectors.toList());
        List<String> hopeLanguages = userHopeLanguageRepository.findAll().stream().map(x -> x.getHopeLanguage().getAbbr()).collect(Collectors.toList());

        Assertions.assertThat(countrys)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("South Korea", "United States", "Samoa", "Kosovo"));
        Assertions.assertThat(languages)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("fy", "xh", "wo", "cy"));
        Assertions.assertThat(hopeLanguages)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("ab", "aa", "sq", "am"));
        Assertions.assertThat(user.getFacebookUrl()).isEqualTo("페이스북주소2");
        Assertions.assertThat(user.getInstagramUrl()).isEqualTo("인스타그램주소2");
        Assertions.assertThat(user.getImageUrl()).isEqualTo("이미지주소2");
        Assertions.assertThat(user.getContent()).isEqualTo("내용2");
    }

    @Test
    public void 카드상세보기() throws Exception {
        //given
        RequestUserVo createVo = RequestUserVo.builder()
                .email("mangchhe@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .content("내용")
                .facebookUrl("페이스북주소")
                .instagramUrl("인스타그램주소")
                .imageUrl("이미지주소")
                .language(Sets.newHashSet(Arrays.asList(
                        "fy",
                        "xh",
                        "yi",
                        "yo"
                )))
                .hopeLanguage(Sets.newHashSet(Arrays.asList(
                        "ab",
                        "aa",
                        "af",
                        "ak"
                )))
                .country(Sets.newHashSet(Arrays.asList(
                        "United States",
                        "South Korea",
                        "Zambia",
                        "Zimbabwe"
                )))
                .build();
        userService.createUser(createVo);
        List<UserEntity> users = userRepository.findAll();
        UserEntity user = users.get(0);
        //when
        UserDetailsDto result = userService.findUserDetails(user.getId());
        //then
        Assertions.assertThat(result.getName()).isEqualTo("이름");
        Assertions.assertThat(result.getImageUrl()).isEqualTo("이미지주소");
        Assertions.assertThat(result.getContent()).isEqualTo("내용");
        Assertions.assertThat(result.getFacebookUrl()).isEqualTo("페이스북주소");
        Assertions.assertThat(result.getInstagramUrl()).isEqualTo("인스타그램주소");
        Assertions.assertThat(result.getFollows()).isEqualTo(0);
        Assertions.assertThat(result.getCountry())
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("United States", "South Korea", "Zambia", "Zimbabwe"));
        Assertions.assertThat(result.getLanguage())
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("fy", "xh", "yi", "yo"));
        Assertions.assertThat(result.getHopeLanguage())
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("ab", "aa", "af", "ak"));

    }

    @Test
    public void 프로필보기() throws Exception {
        //given
        RequestUserVo createVo = RequestUserVo.builder()
                .email("mangchhe@naver.com")
                .password("1234")
                .name("이름")
                .web_email("웹메일")
                .school("학교")
                .content("내용")
                .facebookUrl("페이스북주소")
                .instagramUrl("인스타그램주소")
                .imageUrl("이미지주소")
                .language(Sets.newHashSet(Arrays.asList(
                        "fy",
                        "xh",
                        "yi",
                        "yo"
                )))
                .hopeLanguage(Sets.newHashSet(Arrays.asList(
                        "ab",
                        "aa",
                        "af",
                        "ak"
                )))
                .country(Sets.newHashSet(Arrays.asList(
                        "United States",
                        "South Korea",
                        "Zambia",
                        "Zimbabwe"
                )))
                .build();
        userService.createUser(createVo);
        List<UserEntity> users = userRepository.findAll();
        UserEntity user = users.get(0);
        //when
        UserProfileDto result = userService.findUserProfile(user.getId());
        //then
        Assertions.assertThat(result.getImageUrl()).isEqualTo("이미지주소");
        Assertions.assertThat(result.getContent()).isEqualTo("내용");
        Assertions.assertThat(result.getFacebookUrl()).isEqualTo("페이스북주소");
        Assertions.assertThat(result.getInstagramUrl()).isEqualTo("인스타그램주소");
        Assertions.assertThat(result.getCountry())
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("United States", "South Korea", "Zambia", "Zimbabwe"));
        Assertions.assertThat(result.getLanguage())
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("fy", "xh", "yi", "yo"));
        Assertions.assertThat(result.getHopeLanguage())
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("ab", "aa", "af", "ak"));
    }

}