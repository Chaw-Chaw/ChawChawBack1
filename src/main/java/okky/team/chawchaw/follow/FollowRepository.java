package okky.team.chawchaw.follow;

import okky.team.chawchaw.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    Long countByUserToId(Long id);
    void removeByUserFromAndUserTo(UserEntity userFrom, UserEntity UserTo);
    @Query("select count(f) > 0 from FollowEntity f where f.userFrom.id = :userFrom and f.userTo.id = :userTo")
    Boolean isFollow(@Param("userFrom") Long userFrom,@Param("userTo") Long userTo);
    @Modifying
    @Query("delete from FollowEntity f where f.userFrom = :userFrom or f.userTo = :userTo")
    void deleteByUserFromOrUserTo(@Param("userFrom") UserEntity userFrom, @Param("userTo") UserEntity userTo);

}
