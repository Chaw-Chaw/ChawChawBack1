package okky.team.chawchaw.user;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.chat.ChatService;
import okky.team.chawchaw.chat.dto.ChatMessageDto;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.config.properties.TokenProperties;
import okky.team.chawchaw.like.LikeService;
import okky.team.chawchaw.like.dto.LikeMessageDto;
import okky.team.chawchaw.statistics.log.SearchLogService;
import okky.team.chawchaw.statistics.log.dto.CreateSearchLogDto;
import okky.team.chawchaw.user.dto.*;
import okky.team.chawchaw.user.exception.DiffPasswordException;
import okky.team.chawchaw.user.exception.DiffRefreshTokenException;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
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
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final LikeService likeService;
    private final ChatService chatService;
    private final SearchLogService searchLogService;
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

        userService.duplicateEmail(email);

        return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.EMAIL_NOT_EXIST, false), HttpStatus.OK);


    }

    @GetMapping("")
    public ResponseEntity<List<UserCardDto>> getUserCards(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                          @Valid @ModelAttribute FindUserVo findUserVo,
                                                          HttpServletRequest request) {

        if (findUserVo.getLanguage() != null && !findUserVo.getLanguage().isBlank())
            searchLogService.createSearchLog(new CreateSearchLogDto(principalDetails.getId(), findUserVo.getLanguage()));

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
        result.setIsLike(likeService.isLike(principalDetails.getId(), userId));

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
    public ResponseEntity deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                     @RequestBody DeleteUserDto deleteUserDto){

        deleteUserDto.setUserId(principalDetails.getId());

        try {
            userService.deleteUser(deleteUserDto);
        } catch (DiffPasswordException diffPasswordException) {
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.DIFF_PASSWORD, false), HttpStatus.OK);
        }
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
    public ResponseEntity getAccessToken(HttpServletRequest request) {

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
        } catch (DiffRefreshTokenException diffRefreshTokenException) {
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.CONNECT_ELSEWHERE, false), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/alarm")
    public ResponseEntity getAlarm(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<ChatMessageDto> messages = chatService.findMessagesByUserIdAndRegDate(principalDetails.getId());
        List<LikeMessageDto> likes = likeService.findMessagesByUserFromId(principalDetails.getId());
        return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.FIND_ALARM_SUCCESS, true, new AlarmDto(messages, likes)), HttpStatus.OK);
    }

}
