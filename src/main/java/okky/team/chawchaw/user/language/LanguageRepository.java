package okky.team.chawchaw.user.language;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {

    LanguageEntity findByAbbr(String abbr);


}
