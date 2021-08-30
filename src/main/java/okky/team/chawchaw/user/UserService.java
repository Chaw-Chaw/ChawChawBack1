package okky.team.chawchaw.user;

import okky.team.chawchaw.user.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    Long createUser(CreateUserDto createUserDto);
    Boolean duplicateEmail(String email);
    void deleteUser(Long userId);

    List<UserCardDto> findUserCards(FindUserVo findUserVo);
    UserDetailsDto findUserDetails(Long userId);
    UserProfileDto findUserProfile(UserEntity userEntity);

    UserDetailsDto updateProfile(UpdateUserDto updateUserDto);
    String uploadProfileImage(MultipartFile file, Long userId);
    String deleteProfileImage(String imageUrl, Long userId);

    void saveRefreshToken(Long userId, String refreshToken);
    String verificationRefreshToken(String refreshToken);

    Boolean isUser(String email);
    Long getViews(Long userId);
    void updateViews();
    void checkView(Long userFrom, Long userTo);
}
