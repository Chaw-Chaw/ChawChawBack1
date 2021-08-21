package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    public Long createUser(CreateUserDto createUserDto);
    public Boolean duplicateEmail(String email);
    public void deleteUser(Long userId);

    public List<UserCardDto> findUserCards(FindUserVo findUserVo);
    public UserDetailsDto findUserDetails(Long userId);
    public UserProfileDto findUserProfile(String email);

    public UserDetailsDto updateProfile(UpdateUserDto updateUserDto);
    public String uploadImage(String imageUrl, Long userId);
    public String uploadImage(MultipartFile file, Long userId);
    public String deleteImage(String imageUrl, Long userId);

    public Boolean isUser(String email);
    public Long getViews(Long userId);
    public void updateViews();
    public void checkView(Long userFrom, Long userTo);
}
