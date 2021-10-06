package okky.team.chawchaw.admin;

import okky.team.chawchaw.admin.dto.CreateBlockDto;
import okky.team.chawchaw.admin.dto.DeleteBlockDto;
import okky.team.chawchaw.admin.dto.UpdateProfileDto;
import okky.team.chawchaw.block.BlockEntity;
import okky.team.chawchaw.block.BlockRepository;
import okky.team.chawchaw.user.Role;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.user.country.UserCountryRepository;
import okky.team.chawchaw.user.language.UserHopeLanguageRepository;
import okky.team.chawchaw.user.language.UserLanguageRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
class AdminServiceTest {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCountryRepository userCountryRepository;
    @Autowired
    private UserLanguageRepository userLanguageRepository;
    @Autowired
    private UserHopeLanguageRepository userHopeLanguageRepository;
    @Autowired
    private BlockRepository blockRepository;

    private List<UserEntity> users;

    @BeforeEach
    void initEach() {
        users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            UserEntity user = userRepository.save(UserEntity.builder()
                    .email("test" + i + "@test.kr")
                    .password("1234")
                    .name("이름")
                    .web_email("웹메일")
                    .school("학교")
                    .imageUrl("이미지주소")
                    .build());
            users.add(user);
        }
    }

    @Test
    @DisplayName("test0 프로필 업로드")
    public void update_profile() throws Exception {
        //when
        adminService.updateProfile(UpdateProfileDto.builder()
                .userId(users.get(0).getId())
                .content("내용2").facebookUrl("페이스북주소2").instagramUrl("인스타그램주소2")
                .repCountry("Samoa").repLanguage("cy").repHopeLanguage("am")
                .country(Sets.newHashSet(Arrays.asList(
                        "United States",
                        "South Korea",
                        "Samoa",
                        "Kosovo"
                )))
                .language(Sets.newHashSet(Arrays.asList(
                        "fy",
                        "xh",
                        "cy"
                )))
                .hopeLanguage(Sets.newHashSet(Arrays.asList(
                        "sq",
                        "am"
                )))
                .build());
        //then
        List<String> countrys = userCountryRepository.findByUser(users.get(0)).stream().map(x -> x.getCountry().getName()).collect(Collectors.toList());
        List<String> languages = userLanguageRepository.findByUser(users.get(0)).stream().map(x -> x.getLanguage().getAbbr()).collect(Collectors.toList());
        List<String> hopeLanguages = userHopeLanguageRepository.findByUser(users.get(0)).stream().map(x -> x.getHopeLanguage().getAbbr()).collect(Collectors.toList());

        Assertions.assertThat(countrys)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("South Korea", "United States", "Samoa", "Kosovo"));
        Assertions.assertThat(languages)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("fy", "xh", "cy"));
        Assertions.assertThat(hopeLanguages)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList("sq", "am"));
        Assertions.assertThat(users.get(0).getFacebookUrl()).isEqualTo("페이스북주소2");
        Assertions.assertThat(users.get(0).getInstagramUrl()).isEqualTo("인스타그램주소2");
        Assertions.assertThat(users.get(0).getContent()).isEqualTo("내용2");
        Assertions.assertThat(users.get(0).getRepCountry()).isEqualTo("Samoa");
        Assertions.assertThat(users.get(0).getRepLanguage()).isEqualTo("cy");
        Assertions.assertThat(users.get(0).getRepHopeLanguage()).isEqualTo("am");
        Assertions.assertThat(users.get(0).getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("test0 삭제")
    public void delete_user() throws Exception {
        //when
        adminService.deleteUser(users.get(0).getId());
        //then
        Assertions.assertThat(userRepository.findById(users.get(0).getId())).isEmpty();
    }

    @Test
    @DisplayName("test0 -> test1 차단")
    public void create_block() throws Exception {
        //when
        adminService.createBlock(new CreateBlockDto(users.get(0).getId(), users.get(0).getEmail(), users.get(1).getId()));
        //then
        Assertions.assertThat(blockRepository.findAllByUserFromId(users.get(0).getId())).isNotEmpty();
    }

    @Test
    @DisplayName("test0 -> test1 차단 해제")
    public void delete_block() throws Exception {
        //given
        blockRepository.save(new BlockEntity(users.get(0), users.get(1)));
        //when
        adminService.deleteBlock(new DeleteBlockDto(users.get(0).getId(), users.get(0).getEmail(), users.get(1).getId()));
        //then
        Assertions.assertThat(blockRepository.findAllByUserFromId(users.get(0).getId())).isEmpty();
    }

}