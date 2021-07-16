package okky.team.chawchaw.follow;

import okky.team.chawchaw.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    void removeByUserFromAndUserTo(UserEntity userFrom, UserEntity UserTo);

}
