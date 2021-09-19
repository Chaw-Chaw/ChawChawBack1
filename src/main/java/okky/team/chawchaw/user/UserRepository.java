package okky.team.chawchaw.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>, UserRepositorySupport {

    Optional<UserEntity> findByEmail(String email);

    @Query("select u.views from UserEntity u where u.id = :userId")
    Long findViewsByUserId(@Param("userId") Long userId);

}
