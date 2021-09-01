package okky.team.chawchaw.user;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.config.properties.TokenProperties;
import okky.team.chawchaw.user.dto.*;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.exception.DuplicationUserEmailException;
import okky.team.chawchaw.utils.message.ResponseAuthMessage;
import okky.team.chawchaw.utils.message.ResponseFileMessage;
import okky.team.chawchaw.utils.message.ResponseUserMessage;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final Environment env;
    private final TokenProperties tokenProperties;

    @PostMapping("/signup")
    public ResponseEntity createUser(@Valid @RequestBody CreateUserDto createUserDto){

        String provider = createUserDto.getProvider();

        /**
         * 소셜 로그인일 경우
         * email _ -> &, 소셜 아이디로 로그인을 방지하기 위해서
         */
        if (provider.equals("kakao") || provider.equals("facebook")) {
            createUserDto.setEmail(createUserDto.getEmail().replace("_", "&"));
            createUserDto.setPassword(createUserDto.getEmail() + env.getProperty("social.secret"));
        }

        userService.createUser(createUserDto);

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
                                                          @Valid @ModelAttribute FindUserVo findUserVo,
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
                                            @Valid @RequestBody UpdateUserDto updateUserDto) {
        updateUserDto.setId(principalDetails.getId());

        userService.updateProfile(updateUserDto);

        return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.UPDATE_SUCCESS, true), HttpStatus.OK);

    }

    @PostMapping("/image")
    public ResponseEntity uploadUserImage(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                          @RequestParam MultipartFile file) {
            String result = userService.uploadProfileImage(file, principalDetails.getId());
            if (!result.isEmpty())
                return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.UPLOAD_SUCCESS, true, result), HttpStatus.OK);
            else
                return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.UPLOAD_FAIL, false), HttpStatus.OK);
    }

    @DeleteMapping("/image")
    public ResponseEntity deleteUserImage(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        String result = userService.deleteProfileImage(principalDetails.getImageUrl(), principalDetails.getId());
        if (!result.isEmpty())
            return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.DELETE_SUCCESS, true, result), HttpStatus.OK);
        else
            return new ResponseEntity(DefaultResponseVo.res(ResponseFileMessage.DELETE_FAIL, false), HttpStatus.OK);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity getAccessToken(HttpServletRequest request,
                                         HttpServletResponse response) {

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(tokenProperties.getRefresh().getHeader())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null) {
            return new ResponseEntity(DefaultResponseVo.res(ResponseAuthMessage.NOT_EXIST_REFRESH_TOKEN, false), HttpStatus.UNAUTHORIZED);
        }

        try {
            String accessToken = userService.verificationRefreshToken(refreshToken);
            if (!accessToken.isEmpty()) {
                TokenDto tokenInfo = new TokenDto("JWT", accessToken, tokenProperties.getAccess().getExpirationTime());
                return new ResponseEntity(DefaultResponseVo.res(ResponseAuthMessage.VERIFICATION_SUCCESS, true, tokenInfo), HttpStatus.OK);
            }
            else {
                return new ResponseEntity(DefaultResponseVo.res(ResponseAuthMessage.UNAVAILABLE_REFRESH_TOKEN, false), HttpStatus.UNAUTHORIZED);
            }
        } catch (TokenExpiredException tokenExpiredException) {
            return new ResponseEntity(DefaultResponseVo.res(ResponseAuthMessage.EXPIRE_REFRESH_TOKEN, false), HttpStatus.UNAUTHORIZED);
        } catch (JWTDecodeException jwtDecodeException) {
            return new ResponseEntity(DefaultResponseVo.res(ResponseAuthMessage.WRONG_REFRESH_TOKEN_FORM, false), HttpStatus.UNAUTHORIZED);
        }

    }

}
