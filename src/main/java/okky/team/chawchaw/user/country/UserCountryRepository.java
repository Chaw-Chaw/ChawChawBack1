package okky.team.chawchaw.user.country;

import okky.team.chawchaw.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCountryRepository extends JpaRepository<UserCountryEntity, Long> {

    void deleteByUser(UserEntity userEntity);
    void deleteByUserAndCountry(UserEntity userEntity, CountryEntity countryEntity);
}
