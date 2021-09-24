package okky.team.chawchaw.like;

import okky.team.chawchaw.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    @Modifying
    @Query(value = "insert into likes(user_from, user_to) values (:userFromId, :userToId)", nativeQuery = true)
    void saveByUserFromIdAndUserToId(@Param("userFromId") Long userFromId, @Param("userToId") Long userToId);
    Long countByUserToId(Long id);
    void removeByUserFromIdAndUserToId(Long userFromId, Long userToId);
    @Query("select count(f) > 0 from LikeEntity f where f.userFrom.id = :userFrom and f.userTo.id = :userTo")
    Boolean isLike(@Param("userFrom") Long userFrom, @Param("userTo") Long userTo);
    @Modifying
    @Query("delete from LikeEntity f where f.userFrom = :userFrom or f.userTo = :userTo")
    void deleteByUserFromOrUserTo(@Param("userFrom") UserEntity userFrom, @Param("userTo") UserEntity userTo);

}
