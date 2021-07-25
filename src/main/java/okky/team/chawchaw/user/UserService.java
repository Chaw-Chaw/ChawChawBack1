package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.*;

public interface UserService {

    public Boolean createUser(RequestUserVo requestUserVo);
    public Boolean duplicateEmail(String email);
    public void deleteUser(String email);

    public UserCardDto findUserCards(FindUserVo findUserVo);
    public UserDetailsDto findUserDetails(Long userId);
    public UserProfileDto findUserProfile(Long userId);

    public Boolean updateProfile(RequestUserVo requestUserVo);
}
