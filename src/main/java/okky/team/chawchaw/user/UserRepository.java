package okky.team.chawchaw.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public List<UserEntity> findByEmail(String email);

}
