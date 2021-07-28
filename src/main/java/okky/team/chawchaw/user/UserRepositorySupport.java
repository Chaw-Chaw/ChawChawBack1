package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.FindUserVo;
import okky.team.chawchaw.user.dto.RequestUserVo;
import okky.team.chawchaw.user.dto.UserCardDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserRepositorySupport {

    List<UserCardDto> findAllByElement(FindUserVo findUserVo);

}
