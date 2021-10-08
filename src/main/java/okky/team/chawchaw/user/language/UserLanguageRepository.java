package okky.team.chawchaw.user.language;

import okky.team.chawchaw.statistics.dto.LanguageUsersDto;
import okky.team.chawchaw.user.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserLanguageRepository extends JpaRepository<UserLanguageEntity, Long> {

    @EntityGraph(attributePaths = {"language"}, type = EntityGraph.EntityGraphType.LOAD)
    List<UserLanguageEntity> findByUser(UserEntity userEntity);

    @Query("select new okky.team.chawchaw.statistics.dto.LanguageUsersDto(l.language.abbr, count(l)) " +
            "from UserLanguageEntity l " +
            "group by l.language.id " +
            "order by count(l) desc ")
    List<LanguageUsersDto> findLanguageRanks();

    void deleteByUser(UserEntity userEntity);
    void deleteByUserAndLanguage(UserEntity userEntity, LanguageEntity languageEntity);

}
