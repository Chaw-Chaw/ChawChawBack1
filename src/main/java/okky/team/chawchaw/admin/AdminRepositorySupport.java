package okky.team.chawchaw.admin;

import okky.team.chawchaw.admin.dto.FindUserDto;
import okky.team.chawchaw.admin.dto.UserCardDto;
import org.springframework.data.domain.Page;

public interface AdminRepositorySupport {

    Page<UserCardDto> findAllByElement(FindUserDto findUserDto);

}
