package okky.team.chawchaw.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import okky.team.chawchaw.config.auth.PrincipalDetails;
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

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private SocialService socialService;
    private Environment env;
    private ObjectMapper mapper = new ObjectMapper();


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, Environment env, UserService userService, SocialService socialService) {
        this.authenticationManager = authenticationManager;
        this.env = env;
        this.userService = userService;
        this.socialService = socialService;
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

            /* 카카오 로그인 시 */
            if (user.getProvider().equals("kakao")) {
                socialDto = socialService.verificationKakao(user.getCode());
            }
            /* 페이스북 로그인 시 */
            else if (user.getProvider().equals("facebook")) {
                socialDto = socialService.verificationFacebook(user.getEmail(), user.getAccessToken());
            }
            /* 일반 로그인 시 */
            else {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                );
                authenticate = authenticationManager.authenticate(authenticationToken);
            }

            /* 소셜 로그인 시 */
            if (user.getProvider().equals("kakao") || user.getProvider().equals("facebook")) {
                if (userService.isUser(socialDto.getEmail())) {
                    String token = JWT.create()
                            .withSubject("JwtToken")
                            .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                            .withClaim("email", socialDto.getEmail())
                            .sign(Algorithm.HMAC512(env.getProperty("token.secret")));
                    response.addHeader(env.getProperty("token.header"), env.getProperty("token.prefix") + token);
                    writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.LOGIN_SUCCESS, true, userService.findUserProfile(socialDto.getEmail()))));
                }
                else {
                    writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.NEED_SIGNUP, false, socialDto)));
                }
                }

        } catch (UsernameNotFoundException usernameNotFoundException) {
            writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.ID_NOT_EXIST, false)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.LOGIN_FAIL, false)));
        } finally {
            return authenticate;
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();

        String token = JWT.create()
                .withSubject("JwtToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                .withClaim("email", principal.getUsername())
                .sign(Algorithm.HMAC512(env.getProperty("token.secret")));

        response.addHeader(env.getProperty("token.header"), env.getProperty("token.prefix") + token);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.LOGIN_SUCCESS, true, userService.findUserProfile(principal.getUsername()))));
    }
}
