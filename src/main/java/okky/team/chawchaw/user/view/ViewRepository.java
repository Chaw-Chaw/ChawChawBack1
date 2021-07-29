package okky.team.chawchaw.user.view;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewRepository extends JpaRepository<ViewEntity, Long> {

    ViewEntity findByUserFromIdAndUserToId(Long userFrom, Long userTo);

}