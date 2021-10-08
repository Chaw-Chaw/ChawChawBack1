package okky.team.chawchaw.user.language;

import okky.team.chawchaw.statistics.dto.HopeLanguageUsersDto;
import okky.team.chawchaw.user.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface UserHopeLanguageRepository extends JpaRepository<UserHopeLanguageEntity, Long> {

    @EntityGraph(attributePaths = {"hopeLanguage"}, type = EntityGraph.EntityGraphType.LOAD)
    List<UserHopeLanguageEntity> findByUser(UserEntity userEntity);

    @Query("select new okky.team.chawchaw.statistics.dto.HopeLanguageUsersDto(h.hopeLanguage.abbr, count(h)) " +
            "from UserHopeLanguageEntity h " +
            "group by h.hopeLanguage.id " +
            "order by count(h) desc ")
    List<HopeLanguageUsersDto> findHopeLanguageRanks();

    void deleteByUser(UserEntity userEntity);
    void deleteByUserAndHopeLanguage(UserEntity userEntity, LanguageEntity languageEntity);

}
