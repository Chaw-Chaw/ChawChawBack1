package okky.team.chawchaw.statistics.log;

import okky.team.chawchaw.statistics.log.dto.CreateSearchLogDto;
import okky.team.chawchaw.statistics.log.dto.SearchLanguageUsersDto;

import java.util.List;

public interface SearchLogService {

    void createSearchLog(CreateSearchLogDto createSearchLogDto);

    List<SearchLanguageUsersDto> findLanguageRanks();

}
