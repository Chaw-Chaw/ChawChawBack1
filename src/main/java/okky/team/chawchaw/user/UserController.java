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
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserVo createUserVo){
        Boolean result = userService.createUser(createUserVo);
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
    public ResponseEntity<List<UserCardDto>> getUserCards(@RequestBody RequestUserVo requestUserVo) {
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
    public ResponseEntity<UserDetailsDto> getUserProfile(@PathVariable Long userId) {
        UserDetailsDto result = userService.findUserProfile(userId);
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

    @PutMapping("users/{element}")
    public ResponseEntity<UserDto> updateUserProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @PathVariable String element,
                                            @RequestBody RequestUserVo requestUserVo) {
        Boolean result = false;
        requestUserVo.setId(principalDetails.getId());
        if (element.equals("country")) {
            result = userService.updateCountry(requestUserVo);
        }
        else if (element.equals("language")) {
            result = userService.updateLanguage(requestUserVo);
        }
        else if (element.equals("hope-language")) {
            result = userService.updateHopeLanguage(requestUserVo);
        }
        else if (element.equals("content")) {
            result = userService.updateContent(requestUserVo);
        }
        else if (element.equals("facebook-url")) {
            result = userService.updateFacebookUrl(requestUserVo);
        }
        else if (element.equals("instagram-url")) {
            result = userService.updateInstagramUrl(requestUserVo);
        }
        else if (element.equals("image-url")) {
            result = userService.updateImageUrl(requestUserVo);
        }
        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("users/{element}")
    public ResponseEntity deleteUserProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @PathVariable String element,
                                            @RequestBody RequestUserVo requestUserVo) {
        Boolean result = false;
        requestUserVo.setId(principalDetails.getId());
        if (element.equals("country")) {
            result = userService.deleteCountry(requestUserVo);
        }
        else if (element.equals("language")) {
            result = userService.deleteLanguage(requestUserVo);
        }
        else if (element.equals("hope-language")) {
            result = userService.deleteHopeLanguage(requestUserVo);
        }
        else if (element.equals("facebook-url")) {
            result = userService.deleteFacebookUrl(requestUserVo);
        }
        else if (element.equals("instagram-url")) {
            result = userService.deleteInstagramUrl(requestUserVo);
        }
        else if (element.equals("image-url")) {
            result = userService.deleteImageUrl(requestUserVo);
        }
        if (result){
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

}
