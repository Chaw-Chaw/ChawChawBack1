package okky.team.chawchaw.user.country;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {

    CountryEntity findByName(String name);

}
