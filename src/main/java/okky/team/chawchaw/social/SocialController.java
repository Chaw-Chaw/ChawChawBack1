package okky.team.chawchaw.social;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.social.dto.SocialDto;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.user.UserService;
import okky.team.chawchaw.user.dto.RequestUserVo;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseUserMessage;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;
    private final Environment env;
    private final UserService userService;

    @PostMapping("/users/login/{provider}")
    public ResponseEntity socialLogin(@PathVariable String provider,
                                      @RequestParam(required = false) String code,
                                      @RequestParam(required = false) String userId,
                                      @RequestParam(required = false) String accessToken,
                                      HttpServletResponse response){

        SocialDto socialDto = null;
        Boolean isUser = false;

        if (provider.equals("kakao")) {
            socialDto = socialService.verificationKakao(code);
        }
        else if(provider.equals("facebook")) {
            socialDto = socialService.verificationFacebook(userId, accessToken);
        }

        /* REST API 통신 실패 */
        if (socialDto == null) {
            return new ResponseEntity(DefaultResponseVo.res(
                    ResponseUserMessage.LOGIN_FAIL,
                    false
            ), HttpStatus.OK);
        }else {
            isUser = userService.isUser(socialDto.getEmail());
        }

        /* 아이디가 존재할 경우 */
        if (isUser) {
            String token = JWT.create()
                    .withSubject("JwtToken")
                    .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                    .withClaim("email", socialDto.getEmail())
                    .sign(Algorithm.HMAC512(env.getProperty("token.secret")));
            response.addHeader(env.getProperty("token.header"), env.getProperty("token.prefix") + token);

            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.LOGIN_SUCCESS, true), HttpStatus.OK);
        }
        /* 아이디가 존재하지 않을 경우 */
        else {
            return new ResponseEntity(DefaultResponseVo.res(
                    ResponseUserMessage.NEED_SIGNUP,
                    false,
                    socialDto
            ), HttpStatus.OK);
        }

    }

    @PostMapping("/users/signup/{provider}")
    public ResponseEntity socialSignup(@PathVariable String provider,
                                       @RequestBody RequestUserVo requestUserVo,
                                       HttpServletResponse response) {

        requestUserVo.setPassword(UUID.randomUUID().toString());
        Boolean result = userService.createUser(requestUserVo);

        if (result) {
            String token = JWT.create()
                    .withSubject("JwtToken")
                    .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                    .withClaim("email", requestUserVo.getEmail())
                    .sign(Algorithm.HMAC512(env.getProperty("token.secret")));
            response.addHeader(env.getProperty("token.header"), env.getProperty("token.prefix") + token);
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.CREATED_SUCCESS, true), HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity(DefaultResponseVo.res(ResponseUserMessage.CREATE_FAIL, false), HttpStatus.OK);
        }
    }

}
