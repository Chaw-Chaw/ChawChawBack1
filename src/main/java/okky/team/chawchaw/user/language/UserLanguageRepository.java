package okky.team.chawchaw.user.language;

import okky.team.chawchaw.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLanguageRepository extends JpaRepository<UserLanguageEntity, Long> {

    void deleteByUser(UserEntity userEntity);
    void deleteByUserAndLanguage(UserEntity userEntity, LanguageEntity languageEntity);

}
