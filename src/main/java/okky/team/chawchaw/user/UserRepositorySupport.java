package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.FindUserDto;
import okky.team.chawchaw.user.dto.UserCardDto;

import java.util.List;

public interface UserRepositorySupport {

    List<UserCardDto> findAllByFindUserDto(FindUserDto findUserDto);

}
