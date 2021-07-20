package okky.team.chawchaw.user.language;

import okky.team.chawchaw.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHopeLanguageRepository extends JpaRepository<UserHopeLanguageEntity, Long> {

    void deleteByUser(UserEntity userEntity);
    void deleteByUserAndHopeLanguage(UserEntity userEntity, LanguageEntity languageEntity);

}
