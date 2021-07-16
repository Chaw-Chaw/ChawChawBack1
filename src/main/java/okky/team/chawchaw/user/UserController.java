package okky.team.chawchaw.user;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.user.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("users/signup")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        UserDto user = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("users/email/duplicate")
    public ResponseEntity<Boolean> duplicateEmail(@RequestBody UserDto userDto){
        Boolean result = userService.duplicateEmail(userDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @DeleteMapping("users")
    public ResponseEntity deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails){
        userService.deleteUser(principalDetails.getUsername());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("users/{element}")
    public ResponseEntity<UserDto> updateUserProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @PathVariable String element,
                                            @RequestBody UserDto userDto) {
        UserDto result = null;
        userDto.setId(principalDetails.getId());
        if (element.equals("country")) {
            result = userService.updateCountry(userDto);
        }
        else if (element.equals("language")) {
            result = userService.updateLanguage(userDto);
        }
        else if (element.equals("hope-language")) {
            result = userService.updateHopeLanguage(userDto);
        }
        else if (element.equals("content")) {
            result = userService.updateContent(userDto);
        }
        else if (element.equals("social-url")) {
            result = userService.updateSocialUrl(userDto);
        }
        else if (element.equals("image-url")) {
            result = userService.updateImageUrl(userDto);
        }
        if (result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("users/{element}")
    public ResponseEntity deleteUserProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @PathVariable String element,
                                            @RequestBody UserDto userDto) {
        UserDto result = null;
        userDto.setId(principalDetails.getId());
        if (element.equals("country")) {
            result = userService.deleteCountry(userDto);
        }
        else if (element.equals("language")) {
            result = userService.deleteLanguage(userDto);
        }
        else if (element.equals("hope-language")) {
            result = userService.deleteHopeLanguage(userDto);
        }
        else if (element.equals("social-url")) {
            result = userService.deleteSocialUrl(userDto);
        }
        else if (element.equals("image-url")) {
            result = userService.deleteImageUrl(userDto);
        }
        if (result != null){
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

}
