package okky.team.chawchaw.block;

import okky.team.chawchaw.block.dto.BlockUserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlockRepository extends JpaRepository<BlockEntity, Long> {

    @Query("select new okky.team.chawchaw.block.dto.BlockUserDto(b.userTo.id, b.userTo.name, b.userTo.imageUrl) " +
            "from BlockEntity b " +
            "where b.userFrom.id = :userId")
    List<BlockUserDto> findAllByUserFromId(@Param("userId") Long userId);

    /**
     * 내가 차단한 아이디 번호 조회
     * @param email
     * @return List<Long>
     */
    @Query("select b.userTo.id " +
            "from BlockEntity b " +
            "where b.userFrom.email = :email")
    List<Long> findAllByUserFromEmail(@Param("email") String email);

    /**
     * 내가 차단된 아이디 번호 조회
     * @param email
     * @return List<Long>
     */
    @Query("select b.userFrom.id " +
            "from BlockEntity b " +
            "where b.userTo.email = :email")
    List<Long> findAllByUserToEmail(@Param("email") String email);


    Boolean existsByUserFromIdAndUserToId(Long userFrom, Long userTo);
    void deleteByUserFromIdAndUserToId(Long userFrom, Long userTo);

}
