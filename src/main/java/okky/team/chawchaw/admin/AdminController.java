package okky.team.chawchaw.admin;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.admin.dto.*;
import okky.team.chawchaw.block.exception.ExistBlockException;
import okky.team.chawchaw.block.exception.NotExistBlockException;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.dto.PageResultDto;
import okky.team.chawchaw.utils.message.ResponseBlockMessage;
import okky.team.chawchaw.utils.message.ResponseFileMessage;
import okky.team.chawchaw.utils.message.ResponseUserMessage;
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
    public ResponseEntity updateUserProfile(@Valid @RequestBody UpdateProfileDto updateProfileDto) {

        adminService.updateProfile(updateProfileDto);

        return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.UPDATE_SUCCESS, true), HttpStatus.OK);
    }

    @DeleteMapping("/users")
    public ResponseEntity deleteUser(@Valid @RequestBody DeleteUserDto deleteUserDto) {

        adminService.deleteUser(deleteUserDto.getUserId());

        return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.DELETE_SUCCESS, true), HttpStatus.OK);
    }

    @PostMapping("/users/block")
    public ResponseEntity createBlock(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                      @Valid @RequestBody CreateBlockDto createBlockDto) {

        createBlockDto.setUserFromEmail(principalDetails.getUsername());

        try {
            adminService.createBlock(createBlockDto);
        } catch (ExistBlockException e) {
            return new ResponseEntity(DefaultResponseVo.res(ResponseBlockMessage.EXIST_BLOCK, false), HttpStatus.OK);
        }
        return new ResponseEntity(DefaultResponseVo.res(ResponseBlockMessage.CREATE_BLOCK_SUCCESS, true), HttpStatus.OK);
    }

    @DeleteMapping("/users/block")
    public ResponseEntity deleteBlock(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                      @Valid @RequestBody DeleteBlockDto deleteBlockDto) {

        deleteBlockDto.setUserFromEmail(principalDetails.getUsername());

        try {
            adminService.deleteBlock(deleteBlockDto);
        } catch (NotExistBlockException e) {
            return new ResponseEntity(DefaultResponseVo.res(ResponseBlockMessage.NOT_EXIST_BLOCK, false), HttpStatus.OK);
        }
        return new ResponseEntity(DefaultResponseVo.res(ResponseBlockMessage.DELETE_SUCCESS, true), HttpStatus.OK);
    }

    @PostMapping("/users/image")
    public ResponseEntity updateProfileImage(@Valid @ModelAttribute UploadProfileImageDto uploadProfileImageDto) {

        String result = adminService.updateProfileImage(uploadProfileImageDto);

        if (!result.isEmpty())
            return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.UPLOAD_SUCCESS, true, result), HttpStatus.OK);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.UPLOAD_FAIL, false), HttpStatus.OK);
    }

    @DeleteMapping("/users/image")
    public ResponseEntity deleteProfileImage(@Valid @RequestBody DeleteProfileImageDto deleteProfileImageDto) {

        String result = adminService.deleteProfileImage(deleteProfileImageDto);

        if (!result.isEmpty())
            return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.DELETE_SUCCESS, true, result), HttpStatus.OK);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.DELETE_FAIL, false), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity findUsers(@Valid @ModelAttribute FindUserDto findUserDto) {

        PageResultDto<UserCardDto> result = adminService.findUserCards(findUserDto);

        if (result.getContents().isEmpty())
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.FIND_NOT_EXIST, false), HttpStatus.OK);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.FIND_SUCCESS, true, result), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity findUserDetail(@PathVariable Long userId) {

        UserDetailDto result = adminService.findUserDetail(userId);

        if (result != null)
            return new ResponseEntity(DefaultResponseVo.res(
                    ResponseUserMessage.FIND_SUCCESS,
                    true,
                    result
            ), HttpStatus.OK);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.FIND_FAIL, false), HttpStatus.OK);
    }


}
