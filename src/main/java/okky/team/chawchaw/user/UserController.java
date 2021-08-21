package okky.team.chawchaw.user;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.user.dto.*;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.exception.DuplicationUserEmailException;
import okky.team.chawchaw.utils.message.ResponseFileMessage;
import okky.team.chawchaw.utils.message.ResponseUserMessage;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final Environment env;

    @PostMapping("/signup")
    public ResponseEntity createUser(@RequestBody CreateUserDto createUserDto,
                                     HttpServletResponse response){


        String provider = createUserDto.getProvider();

        if (provider.equals("kakao") || provider.equals("facebook")) {
            createUserDto.setPassword(UUID.randomUUID().toString());
        }

        Long userId = userService.createUser(createUserDto);

        if (provider.equals("kakao") || provider.equals("facebook")) {
            userService.uploadImage(createUserDto.getImageUrl(), userId);
        }

        return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.CREATED_SUCCESS, true), HttpStatus.CREATED);
    }

    @GetMapping("/email/duplicate/{email}")
    public ResponseEntity<Boolean> duplicateEmail(@PathVariable String email){

        if (userService.duplicateEmail(email)) {
            throw new DuplicationUserEmailException();
        }

        return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.EMAIL_NOT_EXIST, false), HttpStatus.OK);


    }

    @GetMapping("")
    public ResponseEntity<List<UserCardDto>> getUserCards(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                          @ModelAttribute FindUserVo findUserVo,
                                                          HttpServletRequest request) {

        findUserVo.getExclude().add(principalDetails.getId());

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("exclude") && !cookie.getValue().isEmpty()) {
                    for (String s : cookie.getValue().split("/")) {
                        findUserVo.getExclude().add(Long.parseLong(s));
                    }
                }
            }
        }

        findUserVo.setSchool(principalDetails.getSchool());

        List<UserCardDto> result = userService.findUserCards(findUserVo);

        if (result.isEmpty())
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.FIND_NOT_EXIST, false), HttpStatus.OK);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.FIND_SUCCESS, true, result), HttpStatus.OK);


    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailsDto> getUserDetails(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                         @PathVariable Long userId) {

        userService.checkView(principalDetails.getId(), userId);

        UserDetailsDto result = userService.findUserDetails(userId);
        result.setViews(userService.getViews(userId));

        if (result != null)
            return new ResponseEntity(DefaultResponseVo.res(
                    ResponseUserMessage.FIND_SUCCESS,
                    true,
                    result
            ), HttpStatus.OK);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.FIND_FAIL, false), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails){
        userService.deleteUser(principalDetails.getId());

        return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.DELETE_SUCCESS, true), HttpStatus.OK);
    }

    @PostMapping("/profile")
    public ResponseEntity updateUserProfile(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @RequestBody UpdateUserDto updateUserDto) {
        updateUserDto.setId(principalDetails.getId());

        userService.updateProfile(updateUserDto);

        return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.UPDATE_SUCCESS, true), HttpStatus.OK);

    }

    @GetMapping("/image")
    public ResponseEntity findUserImage(@RequestParam String imageUrl) {
        byte[] result = null;
        HttpHeaders header = new HttpHeaders();

        try {
            File file = new File(env.getProperty("user.profile.image.path") + File.separator + imageUrl);
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            result = FileCopyUtils.copyToByteArray(file);

        } catch (Exception e) {
            return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.FIND_FAIL, false), HttpStatus.OK);
        }
        return new ResponseEntity(result, header, HttpStatus.OK);
    }

    @PostMapping("/image")
    public ResponseEntity uploadUserImage(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                          @RequestParam MultipartFile file) {
            String result = userService.uploadImage(file, principalDetails.getId());
            if (!result.isEmpty())
                return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.UPLOAD_SUCCESS, true, result), HttpStatus.OK);
            else
                return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.UPLOAD_FAIL, false), HttpStatus.OK);
    }

    @DeleteMapping("/image")
    public ResponseEntity deleteUserImage(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        String result = userService.deleteImage(principalDetails.getImageUrl(), principalDetails.getId());
        if (!result.isEmpty())
            return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.DELETE_SUCCESS, true, result), HttpStatus.OK);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.DELETE_FAIL, false), HttpStatus.OK);
    }

}
