package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.FindUserVo;
import okky.team.chawchaw.user.dto.UserCardDto;

import java.util.List;

public interface UserRepositorySupport {

    List<UserCardDto> findAllByElement(FindUserVo findUserVo);

}
