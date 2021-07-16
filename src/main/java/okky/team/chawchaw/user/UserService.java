package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.UserDetailsDto;
import okky.team.chawchaw.user.dto.UserDto;

public interface UserService {

    public UserDto createUser(UserDto userDto);
    public Boolean duplicateEmail(String email);
    public void deleteUser(String email);

    public UserDetailsDto findUserDetails(Long userId);
    public UserDetailsDto findUserProfile(Long userId);

    public UserDto updateCountry(UserDto userDto);
    public UserDto updateLanguage(UserDto userDto);
    public UserDto updateHopeLanguage(UserDto userDto);
    public UserDto updateContent(UserDto userDto);
    public UserDto updateSocialUrl(UserDto userDto);
    public UserDto updateImageUrl(UserDto userDto);

    public UserDto deleteCountry(UserDto userDto);
    public UserDto deleteLanguage(UserDto userDto);
    public UserDto deleteHopeLanguage(UserDto userDto);
    public UserDto deleteSocialUrl(UserDto userDto);
    public UserDto deleteImageUrl(UserDto userDto);
}
