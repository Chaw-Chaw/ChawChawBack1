package okky.team.chawchaw.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public Optional<UserEntity> findByEmail(String email);

}
