package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.FindUserVo;
import okky.team.chawchaw.user.dto.RequestUserVo;
import org.springframework.data.domain.Page;

public interface UserRepositorySupport {

    Page<UserEntity> findAllByElement(FindUserVo findUserVo);

}
