package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    public Boolean createUser(CreateUserDto createUserDto);
    public Boolean duplicateEmail(String email);
    public void deleteUser(String email);

    public List<UserCardDto> findUserCards(FindUserVo findUserVo);
    public UserDetailsDto findUserDetails(Long userId, Long userId2);
    public UserProfileDto findUserProfile(String email);

    public Boolean updateProfile(UpdateUserDto updateUserDto);
    public String uploadImage(MultipartFile file, Long userId);
    public String deleteImage(String imageUrl, Long userId);

    public Boolean isUser(String email);
}
