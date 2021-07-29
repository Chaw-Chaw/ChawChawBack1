package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.*;

import java.util.List;

public interface UserService {

    public Boolean createUser(RequestUserVo requestUserVo);
    public Boolean duplicateEmail(String email);
    public void deleteUser(String email);

    public List<UserCardDto> findUserCards(FindUserVo findUserVo);
    public UserDetailsDto findUserDetails(Long userId, Long userId2);
    public UserProfileDto findUserProfile(Long userId);

    public Boolean updateProfile(RequestUserVo requestUserVo);

    public Boolean isUser(String email);
}
