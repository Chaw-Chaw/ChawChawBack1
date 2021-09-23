package okky.team.chawchaw.block;

import okky.team.chawchaw.block.dto.BlockSessionDto;
import okky.team.chawchaw.block.dto.BlockUserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlockRepository extends JpaRepository<BlockEntity, Long> {

    @Modifying
    @Query(value = "insert into block(user_from, user_to) values (:userFromId, :userToId)", nativeQuery = true)
    void saveByUserFromIdAndUserToId(@Param("userFromId") Long userFromId, @Param("userToId") Long userToId);

    @Query("select new okky.team.chawchaw.block.dto.BlockUserDto(b.userTo.id, b.userTo.name, b.userTo.imageUrl) " +
            "from BlockEntity b " +
            "where b.userFrom.id = :userId")
    List<BlockUserDto> findAllByUserFromId(@Param("userId") Long userId);

    @Query("select b.userTo.id " +
            "from BlockEntity b " +
            "where b.userFrom.id = :userId")
    List<Long> findUserToIdsByUserFromId(@Param("userId") Long userId);

    /**
     * 내가 차단한 아이디 번호 조회
     * @param email
     * @return List<Long>
     */
    @Query("select new okky.team.chawchaw.block.dto.BlockSessionDto(b.userTo.id, b.regDate) " +
            "from BlockEntity b " +
            "where b.userFrom.email = :email")
    List<BlockSessionDto> findSessionDtoByUserFromEmail(@Param("email") String email);

    /**
     * 내가 차단된 아이디 번호 조회
     * @param email
     * @return List<Long>
     */
    @Query("select new okky.team.chawchaw.block.dto.BlockSessionDto(b.userFrom.id, b.regDate) " +
            "from BlockEntity b " +
            "where b.userTo.email = :email")
    List<BlockSessionDto> findSessionDtoByUserToEmail(@Param("email") String email);

    /**
     * 차단 했거나 차단 되었는지에 대한 검증
     * @param userFromId
     * @param userToId
     * @return 차단 여부
     */
    @Query("select count(b) > 0 " +
            "from BlockEntity b " +
            "where (b.userFrom.id = :userFromId and b.userTo.id = :userToId) " +
            "or (b.userFrom.id = :userToId and b.userTo.id = :userFromId)")
    Boolean existsByUserIds(@Param("userFromId") Long userFromId, @Param("userToId") Long userToId);
    Boolean existsByUserFromIdAndUserToId(Long userFrom, Long userTo);
    void deleteByUserFromIdAndUserToId(Long userFrom, Long userTo);

}
