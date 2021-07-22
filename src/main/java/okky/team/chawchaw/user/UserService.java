package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.RequestUserVo;
import okky.team.chawchaw.user.dto.UserDetailsDto;

public interface UserService {

    public Boolean createUser(RequestUserVo requestUserVo);
    public Boolean duplicateEmail(String email);
    public void deleteUser(String email);

    public UserDetailsDto findUserDetails(Long userId);
    public UserDetailsDto findUserProfile(Long userId);

    public Boolean updateProfile(RequestUserVo requestUserVo);
}
