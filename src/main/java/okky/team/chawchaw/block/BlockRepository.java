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

    Boolean existsByUserFromIdAndUserToId(Long userFrom, Long userTo);
    void deleteByUserFromIdAndUserToId(Long userFrom, Long userTo);

}
