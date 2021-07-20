package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.CreateUserVo;
import okky.team.chawchaw.user.dto.RequestUserVo;
import okky.team.chawchaw.user.dto.UserDetailsDto;
import okky.team.chawchaw.user.dto.UserDto;

public interface UserService {

    public Boolean createUser(CreateUserVo createUserVo);
    public Boolean duplicateEmail(String email);
    public void deleteUser(String email);

    public UserDetailsDto findUserDetails(Long userId);
    public UserDetailsDto findUserProfile(Long userId);

    public Boolean updateCountry(RequestUserVo requestUserVo);
    public Boolean updateLanguage(RequestUserVo requestUserVo);
    public Boolean updateHopeLanguage(RequestUserVo requestUserVo);
    public Boolean updateContent(RequestUserVo requestUserVo);
    public Boolean updateFacebookUrl(RequestUserVo requestUserVo);
    public Boolean updateInstagramUrl(RequestUserVo requestUserVo);
    public Boolean updateImageUrl(RequestUserVo requestUserVo);

    public Boolean deleteCountry(RequestUserVo requestUserVo);
    public Boolean deleteLanguage(RequestUserVo requestUserVo);
    public Boolean deleteHopeLanguage(RequestUserVo requestUserVo);
    public Boolean deleteFacebookUrl(RequestUserVo requestUserVo);
    public Boolean deleteInstagramUrl(RequestUserVo requestUserVo);
    public Boolean deleteImageUrl(RequestUserVo requestUserVo);
}
