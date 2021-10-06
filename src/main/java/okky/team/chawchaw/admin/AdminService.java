package okky.team.chawchaw.admin;

import okky.team.chawchaw.admin.dto.*;
import okky.team.chawchaw.user.dto.UserCardDto;
import okky.team.chawchaw.user.dto.UserDetailsDto;

import java.util.List;

public interface AdminService {

    List<UserCardDto> findUsers(FindUserDto findUserDto);
    UserDetailDto findUserDetail(Long userId);
    UserDetailsDto updateProfile(UpdateProfileDto updateProfileDto);
    void deleteUser(Long userId);

    void createBlock(CreateBlockDto createBlockDto);
    void deleteBlock(DeleteBlockDto deleteBlockDto);
    void updateBlockSession(String email);

    String updateProfileImage(UploadProfileImageDto profileImageDto);
    String deleteProfileImage(DeleteProfileImageDto deleteProfileImageDto);

}
