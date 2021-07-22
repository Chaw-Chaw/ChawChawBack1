package okky.team.chawchaw.user.language;

import okky.team.chawchaw.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface UserLanguageRepository extends JpaRepository<UserLanguageEntity, Long> {

    List<UserLanguageEntity> findByUser(UserEntity userEntity);
    void deleteByUser(UserEntity userEntity);
    void deleteByUserAndLanguage(UserEntity userEntity, LanguageEntity languageEntity);

}
