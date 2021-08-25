package okky.team.chawchaw.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.config.properties.TokenProperties;
import okky.team.chawchaw.social.SocialService;
import okky.team.chawchaw.social.dto.SocialDto;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserService;
import okky.team.chawchaw.user.dto.LoginUserDto;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseUserMessage;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private SocialService socialService;
    private ObjectMapper mapper = new ObjectMapper();
    private TokenProperties tokenProperties;
    private Environment env;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, Environment env, UserService userService, SocialService socialService, TokenProperties tokenProperties) {
        this.authenticationManager = authenticationManager;
        this.env = env;
        this.userService = userService;
        this.socialService = socialService;
        this.tokenProperties = tokenProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        PrintWriter writer = null;
        Authentication authenticate = null;

        try {

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            writer = response.getWriter();
            SocialDto socialDto = null;

            LoginUserDto user = mapper.readValue(request.getInputStream(), LoginUserDto.class);

            /**
             * 소셜 로그인 시 토큰이 아닌 방식으로 접근하는 것을 제한
             */
            if (user.getEmail().startsWith("facebook&") || user.getEmail().startsWith("kakao&")) {
                writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.WRONG_LOGIN_ACCESS, false)));
                return null;
            }

            /* 카카오 로그인 시 */
            if (user.getProvider().equals("kakao")) {
                socialDto = socialService.verificationKakao(user.getKakaoToken());
            }
            /* 페이스북 로그인 시 */
            else if (user.getProvider().equals("facebook")) {
                socialDto = socialService.verificationFacebook(user.getEmail(), user.getFacebookToken());
            }

            if (socialDto != null) {
                user.setEmail(socialDto.getEmail().replace("_", "&"));
                user.setPassword(user.getEmail() + env.getProperty("social.secret"));
                if (!userService.isUser(user.getEmail())) {
                    writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.NEED_SIGNUP, false, socialDto)));
                    return null;
                }
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    user.getPassword()
            );

            authenticate = authenticationManager.authenticate(authenticationToken);

        } catch (UsernameNotFoundException usernameNotFoundException) {
            writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.ID_NOT_EXIST, false)));
        } catch (Exception e) {
            writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.LOGIN_FAIL, false)));
        } finally {
            return authenticate;
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        String refreshKey = UUID.randomUUID().toString();
        userService.saveRefreshToken(principal.getId(), refreshKey);

        String accessToken = JWT.create()
                .withSubject("AccessToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenProperties.getAccess().getExpirationTime()))
                .withClaim("email", principal.getUsername())
                .sign(Algorithm.HMAC512(tokenProperties.getSecret()));

        String refreshToken = JWT.create()
                .withSubject("RefreshToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenProperties.getRefresh().getExpirationTime()))
                .withClaim("key", principal.getId())
                .withClaim("value", refreshKey)
                .sign(Algorithm.HMAC512(tokenProperties.getSecret()));

        response.addHeader(tokenProperties.getAccess().getHeader(), tokenProperties.getPrefix() + accessToken);
        response.addHeader(tokenProperties.getRefresh().getHeader(), tokenProperties.getPrefix() + refreshToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.LOGIN_SUCCESS, true, userService.findUserProfile(principal.getUserEntity()))));
    }

}
