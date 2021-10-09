package okky.team.chawchaw.statistics.log;

import okky.team.chawchaw.statistics.log.dto.SearchLanguageUsersDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchLogRepository extends JpaRepository<SearchLogEntity, Long> {

    @Query("select new okky.team.chawchaw.statistics.log.dto.SearchLanguageUsersDto(s.languageAbbr, count(s)) " +
            "from SearchLogEntity s " +
            "group by s.languageAbbr " +
            "order by count(s) desc ")
    List<SearchLanguageUsersDto> findLanguageRanks();

}
