package okky.team.chawchaw.user;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.user.dto.*;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseUserMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("users/signup")
    public ResponseEntity createUser(@RequestBody RequestUserVo requestUserVo){
        Boolean result = userService.createUser(requestUserVo);

        if (result)
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.CREATED_SUCCESS, true), HttpStatus.CREATED);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.CREATE_FAIL, false), HttpStatus.CREATED);

    }

    @GetMapping("users/email/duplicate")
    public ResponseEntity<Boolean> duplicateEmail(@RequestBody UserDto userDto){
        Boolean result = userService.duplicateEmail(userDto.getEmail());

        if (result)
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.EMAIL_EXIST, true), HttpStatus.OK);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.EMAIL_NOT_EXIST, false), HttpStatus.OK);

    }

    @GetMapping("users")
    public ResponseEntity<List<UserCardDto>> getUserCards(@ModelAttribute FindUserVo findUserVo) {

        try {

            List<UserCardDto> result = userService.findUserCards(findUserVo);

            if (result.isEmpty())
                return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.FIND_NOT_EXIST, false), HttpStatus.OK);
            else
                return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.FIND_SUCCESS, true, result), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.FIND_FAIL, false), HttpStatus.OK);
        }


    }

    @GetMapping("users/{userId}")
    public ResponseEntity<UserDetailsDto> getUserDetails(@PathVariable Long userId) {
        UserDetailsDto result = userService.findUserDetails(userId);
        if (result != null)
            return new ResponseEntity(DefaultResponseVo.res(
                    ResponseUserMessage.FIND_SUCCESS,
                    true,
                    result
            ), HttpStatus.OK);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.FIND_FAIL, false), HttpStatus.OK);
    }

    @GetMapping("users/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        UserProfileDto result = userService.findUserProfile(principalDetails.getId());
        if (result != null)
            return new ResponseEntity(DefaultResponseVo.res(
                    ResponseUserMessage.FIND_SUCCESS,
                    true,
                    result
            ), HttpStatus.OK);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.FIND_FAIL, false), HttpStatus.OK);
    }


    @DeleteMapping("users")
    public ResponseEntity deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails){
        userService.deleteUser(principalDetails.getUsername());

        return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.DELETE_SUCCESS, true), HttpStatus.OK);
    }

    @PostMapping("users/profile")
    public ResponseEntity<UserDto> updateUserProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @RequestBody RequestUserVo requestUserVo) {
        requestUserVo.setId(principalDetails.getId());

        Boolean result = userService.updateProfile(requestUserVo);

        if (result)
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.UPDATE_SUCCESS, true), HttpStatus.OK);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.UPDATE_FAIL, false), HttpStatus.OK);

    }


}
