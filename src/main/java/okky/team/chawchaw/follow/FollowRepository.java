package okky.team.chawchaw.follow;

import okky.team.chawchaw.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    void removeByUserFromAndUserTo(UserEntity userFrom, UserEntity UserTo);
    @Modifying
    @Query("delete from FollowEntity f where f.userFrom = :userFrom or f.userTo = :userTo")
    void deleteByUserFromOrUserTo(UserEntity userFrom, UserEntity userTo);
    Long countByUserTo(UserEntity userTo);

}
