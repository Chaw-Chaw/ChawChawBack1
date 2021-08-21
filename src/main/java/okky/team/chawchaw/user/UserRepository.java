package okky.team.chawchaw.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>, UserRepositorySupport {

    public Optional<UserEntity> findByEmail(String email);

    @Query("select u.views from UserEntity u where u.id = :userId")
    public Long findViewsByUserId(Long userId);

}
