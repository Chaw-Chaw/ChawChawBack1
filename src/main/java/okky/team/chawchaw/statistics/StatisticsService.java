package okky.team.chawchaw.statistics;

import okky.team.chawchaw.statistics.dto.HopeLanguageUsersDto;
import okky.team.chawchaw.statistics.dto.LanguageUsersDto;
import okky.team.chawchaw.statistics.dto.SchoolUsersDto;

import java.util.List;

public interface StatisticsService {

    List<SchoolUsersDto> findSchoolRanks();
    List<LanguageUsersDto> findLanguageRanks();
    List<HopeLanguageUsersDto> findHopeLanguageRanks();

}
