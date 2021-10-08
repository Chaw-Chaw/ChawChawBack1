package okky.team.chawchaw.statistics;

import okky.team.chawchaw.statistics.dto.HopeLanguageUsersDto;
import okky.team.chawchaw.statistics.dto.LanguageUsersDto;
import okky.team.chawchaw.statistics.dto.SchoolUsersDto;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class StatisticsServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatisticsService statisticsService;

    private List<UserEntity> users;

    @BeforeEach
    void initEach() {
        users = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            for (int j = 100; j > 90 + i; j--) {
                UserEntity user = userRepository.save(UserEntity.builder()
                        .email("test" + i + "_" + j + "@test.kr")
                        .password("1234")
                        .name("테스터" + i)
                        .web_email("웹메일")
                        .school("학교" + i)
                        .imageUrl("이미지주소")
                        .build());
                users.add(user);
            }
        }
    }

    @Test
    @DisplayName("많은 사람들이 이용하는 학교 순위")
    public void find_school_rank() throws Exception {
        //when
        List<SchoolUsersDto> result = statisticsService.findSchoolRanks();
        //then
        Assertions.assertThat(result.size()).isEqualTo(10);
        Assertions.assertThat(result)
                .isSortedAccordingTo(Comparator.comparing(SchoolUsersDto::getUserCnt).reversed());
    }

    @Test
    @DisplayName("사람들이 구사할 수 있는 언어 순위")
    public void find_language_rank() throws Exception {
        //when
        List<LanguageUsersDto> result = statisticsService.findLanguageRanks();
        //then
        Assertions.assertThat(result.size()).isEqualTo(10);
        Assertions.assertThat(result)
                .isSortedAccordingTo(Comparator.comparing(LanguageUsersDto::getUserCnt).reversed());
    }

    @Test
    @DisplayName("사람들이 배우길 희망하는 언어 순위")
    public void find_hopeLanguage_rank() throws Exception {
        //when
        List<HopeLanguageUsersDto> result = statisticsService.findHopeLanguageRanks();
        //then
        Assertions.assertThat(result.size()).isEqualTo(10);
        Assertions.assertThat(result)
                .isSortedAccordingTo(Comparator.comparing(HopeLanguageUsersDto::getUserCnt).reversed());
    }

}