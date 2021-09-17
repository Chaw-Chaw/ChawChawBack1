package okky.team.chawchaw.block;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<BlockEntity, Long> {

    Boolean existsByUserFromIdAndUserToId(Long userFrom, Long userTo);
    void deleteByUserFromIdAndUserToId(Long userFrom, Long userTo);

}
