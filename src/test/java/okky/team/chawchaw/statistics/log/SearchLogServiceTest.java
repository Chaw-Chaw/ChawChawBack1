package okky.team.chawchaw.statistics.log;

import okky.team.chawchaw.statistics.log.dto.SearchLanguageUsersDto;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.user.language.LanguageEntity;
import okky.team.chawchaw.user.language.LanguageRepository;
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
import java.util.Random;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class SearchLogServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private SearchLogRepository searchLogRepository;
    @Autowired
    private SearchLogService searchLogService;

    List<UserEntity> users;

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
        Random rd = new Random();
        List<LanguageEntity> languages = languageRepository.findAll();
        for (int i = 0; i < 100; i++) {
            searchLogRepository.save(new SearchLogEntity(users.get(0).getId(), languages.get(rd.nextInt(104)).getAbbr()));
        }
    }

    @Test
    @DisplayName("사람들이 많이 검색한 언어 순위")
    public void search_language_rank() throws Exception {
        //when
        List<SearchLanguageUsersDto> result = searchLogService.findLanguageRanks();
        //then
        Assertions.assertThat(result)
                .isSortedAccordingTo(Comparator.comparing(SearchLanguageUsersDto::getUserCnt).reversed());
    }

}