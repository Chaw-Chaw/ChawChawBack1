package okky.team.chawchaw.statistics;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.statistics.dto.HopeLanguageUsersDto;
import okky.team.chawchaw.statistics.dto.LanguageUsersDto;
import okky.team.chawchaw.statistics.dto.SchoolUsersDto;
import okky.team.chawchaw.statistics.log.SearchLogService;
import okky.team.chawchaw.statistics.log.dto.SearchLanguageUsersDto;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseGlobalMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final SearchLogService searchLogService;

    @GetMapping("/users/rank/school")
    public ResponseEntity<?> findSchoolRanks() {

        List<SchoolUsersDto> result = statisticsService.findSchoolRanks();
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200, result), HttpStatus.OK);
    }

    @GetMapping("/users/rank/language")
    public ResponseEntity<?> findLanguageRanks() {

        List<LanguageUsersDto> result = statisticsService.findLanguageRanks();
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200, result), HttpStatus.OK);
    }

    @GetMapping("/users/rank/hopeLanguage")
    public ResponseEntity<?> findHopeLanguageRanks() {

        List<HopeLanguageUsersDto> result = statisticsService.findHopeLanguageRanks();
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200, result), HttpStatus.OK);
    }

    @GetMapping("/users/rank/visited/language")
    public ResponseEntity<?> findSearchLanguageRanks() {

        List<SearchLanguageUsersDto> result = searchLogService.findLanguageRanks();
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200, result), HttpStatus.OK);
    }

}
