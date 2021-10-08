package okky.team.chawchaw.user;

import okky.team.chawchaw.statistics.dto.SchoolUsersDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>, UserRepositorySupport {

    Optional<UserEntity> findByEmail(String email);

    @Query("select new okky.team.chawchaw.statistics.dto.SchoolUsersDto(u.school, count(u)) " +
            "from UserEntity u " +
            "group by u.school " +
            "order by count(u) desc ")
    List<SchoolUsersDto> findSchoolRanks();

    @Query("select u.views from UserEntity u where u.id = :userId")
    Long findViewsByUserId(@Param("userId") Long userId);

}
