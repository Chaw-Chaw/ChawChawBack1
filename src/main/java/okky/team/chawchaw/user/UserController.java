package okky.team.chawchaw.user;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.user.dto.*;
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
    public ResponseEntity<UserDto> createUser(@RequestBody RequestUserVo requestUserVo){
        Boolean result = userService.createUser(requestUserVo);
        if (result)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("users/email/duplicate")
    public ResponseEntity<Boolean> duplicateEmail(@RequestBody UserDto userDto){
        Boolean result = userService.duplicateEmail(userDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @GetMapping("users")
    public ResponseEntity<List<UserCardDto>> getUserCards(@ModelAttribute FindUserVo findUserVo) {

        userService.findUserCards(findUserVo);

        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<UserDetailsDto> getUserDetails(@PathVariable Long userId) {
        UserDetailsDto result = userService.findUserDetails(userId);
        if (result != null)
            return ResponseEntity.status(HttpStatus.OK).body(result);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("users/{userId}/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long userId) {
        UserProfileDto result = userService.findUserProfile(userId);
        if (result != null)
            return ResponseEntity.status(HttpStatus.OK).body(result);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("users")
    public ResponseEntity deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails){
        userService.deleteUser(principalDetails.getUsername());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("users/profile")
    public ResponseEntity<UserDto> updateUserProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @RequestBody RequestUserVo requestUserVo) {
        Boolean result = false;
        requestUserVo.setId(principalDetails.getId());

        result = userService.updateProfile(requestUserVo);

        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
