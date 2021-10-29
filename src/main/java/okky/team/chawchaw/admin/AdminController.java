package okky.team.chawchaw.admin;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.admin.dto.*;
import okky.team.chawchaw.block.exception.ExistBlockException;
import okky.team.chawchaw.block.exception.NotExistBlockException;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.dto.PageResultDto;
import okky.team.chawchaw.utils.message.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/users/profile")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody UpdateProfileDto updateProfileDto) {

        adminService.updateProfile(updateProfileDto);

        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200), HttpStatus.OK);
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(@Valid @RequestBody DeleteUserDto deleteUserDto) {

        adminService.deleteUser(deleteUserDto.getUserId());

        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200), HttpStatus.OK);
    }

    @PostMapping("/users/block")
    public ResponseEntity<?> createBlock(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                      @Valid @RequestBody CreateBlockDto createBlockDto) {

        createBlockDto.setUserFromEmail(principalDetails.getUsername());

        try {
            adminService.createBlock(createBlockDto);
        } catch (ExistBlockException e) {
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseAdminMessage.A401), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200), HttpStatus.OK);
    }

    @DeleteMapping("/users/block")
    public ResponseEntity<?> deleteBlock(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                      @Valid @RequestBody DeleteBlockDto deleteBlockDto) {

        deleteBlockDto.setUserFromEmail(principalDetails.getUsername());

        try {
            adminService.deleteBlock(deleteBlockDto);
        } catch (NotExistBlockException e) {
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseAdminMessage.A402), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200), HttpStatus.OK);
    }

    @PostMapping("/users/image")
    public ResponseEntity<?> updateProfileImage(@Valid @ModelAttribute UploadProfileImageDto uploadProfileImageDto) {

        String result = adminService.updateProfileImage(uploadProfileImageDto);

        if (!result.isEmpty())
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200, result), HttpStatus.OK);
        else
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseAdminMessage.A403), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/users/image")
    public ResponseEntity<?> deleteProfileImage(@Valid @RequestBody DeleteProfileImageDto deleteProfileImageDto) {

        String result = adminService.deleteProfileImage(deleteProfileImageDto);

        if (!result.isEmpty())
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200, result), HttpStatus.OK);
        else
            return new ResponseEntity<>(DefaultResponseVo.res(ResponseAdminMessage.A404), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/users")
    public ResponseEntity<?> findUsers(@Valid @ModelAttribute FindUserDto findUserDto) {

        PageResultDto<UserCardDto> result = adminService.findUserCards(findUserDto);

        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200, result), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> findUserDetail(@PathVariable Long userId) {

        UserDetailDto result = adminService.findUserDetail(userId);

        return new ResponseEntity<>(DefaultResponseVo.res(ResponseGlobalMessage.G200, result), HttpStatus.OK);
    }


}
