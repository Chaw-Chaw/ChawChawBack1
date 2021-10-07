package okky.team.chawchaw.admin;

import okky.team.chawchaw.admin.dto.*;
import okky.team.chawchaw.user.dto.UserDetailsDto;
import okky.team.chawchaw.utils.dto.PageResultDto;

public interface AdminService {

    PageResultDto<UserCardDto> findUserCards(FindUserDto findUserDto);
    UserDetailDto findUserDetail(Long userId);
    UserDetailsDto updateProfile(UpdateProfileDto updateProfileDto);
    void deleteUser(Long userId);

    void createBlock(CreateBlockDto createBlockDto);
    void deleteBlock(DeleteBlockDto deleteBlockDto);
    void updateBlockSession(String email);

    String updateProfileImage(UploadProfileImageDto profileImageDto);
    String deleteProfileImage(DeleteProfileImageDto deleteProfileImageDto);

}
