package okky.team.chawchaw.social;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.social.dto.SocialDto;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.user.dto.UserDto;
import okky.team.chawchaw.utils.DtoToEntity;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseSocialMessage;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;

    @PostMapping("/users/login/{provider}")
    public ResponseEntity socialLogin(@PathVariable String provider,
                                      @RequestBody(required = false) UserDto userDto,
                                      @RequestParam(required = false) String code,
                                      @RequestParam(required = false) String userId,
                                      @RequestParam(required = false) String accessToken,
                                      HttpServletResponse response){

        SocialDto socialDto = null;

        if (provider.equals("kakao")) {
            socialDto = socialService.verificationKakao(code);
        }
        else if(provider.equals("facebook")) {
            socialDto = socialService.verificationFacebook(userId, accessToken);
        }
        else {
            return new ResponseEntity(DefaultResponseVo.res(ResponseSocialMessage.LOGIN_FAIL, false), HttpStatus.OK);
        }

        UserEntity user = userRepository.findByEmail(socialDto.getEmail()).orElseGet(null);

        if (user != null) {
            userDto.setEmail(socialDto.getEmail());
            userDto.setName(socialDto.getName());
            userDto.setImageUrl(socialDto.getImageUrl());
            userDto.setPassword(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()));
            UserEntity userEntity = DtoToEntity.userDtoToEntity(userDto);
            userRepository.save(userEntity);
        }
        String token = JWT.create()
                .withSubject("JwtToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                .withClaim("email", socialDto.getEmail())
                .sign(Algorithm.HMAC512(env.getProperty("token.secret")));

        response.addHeader(env.getProperty("token.header"), env.getProperty("token.prefix") + token);

        return new ResponseEntity(DefaultResponseVo.res(ResponseSocialMessage.LOGIN_SUCCESS, true), HttpStatus.OK);
    }

}
