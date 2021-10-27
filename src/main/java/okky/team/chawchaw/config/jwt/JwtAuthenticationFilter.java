package okky.team.chawchaw.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import okky.team.chawchaw.block.BlockRepository;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.config.properties.TokenProperties;
import okky.team.chawchaw.social.SocialService;
import okky.team.chawchaw.social.dto.SocialDto;
import okky.team.chawchaw.user.UserService;
import okky.team.chawchaw.user.dto.LoginUserDto;
import okky.team.chawchaw.user.dto.TokenDto;
import okky.team.chawchaw.user.dto.UserProfileTokenDto;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseGlobalMessage;
import okky.team.chawchaw.utils.message.ResponseUserMessage;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private SocialService socialService;
    private Environment env;
    private TokenProperties tokenProperties;
    private JwtTokenProvider jwtTokenProvider;
    private TokenRedisRepository tokenRedisRepository;
    private BlockRepository blockRepository;
    private ObjectMapper mapper = new ObjectMapper();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, SocialService socialService, Environment env, TokenProperties tokenProperties, JwtTokenProvider jwtTokenProvider, TokenRedisRepository tokenRedisRepository, BlockRepository blockRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.socialService = socialService;
        this.env = env;
        this.tokenProperties = tokenProperties;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenRedisRepository = tokenRedisRepository;
        this.blockRepository = blockRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        PrintWriter writer = null;
        Authentication authenticate = null;

        try {

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(401);
            writer = response.getWriter();
            SocialDto socialDto = null;

            LoginUserDto user = mapper.readValue(request.getInputStream(), LoginUserDto.class);

            /**
             * 소셜 로그인 시 토큰이 아닌 방식으로 접근하는 것을 제한
             */
            if (user.getEmail() != null && (user.getEmail().startsWith("f&") || user.getEmail().startsWith("k&"))) {
                writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.U401)));
                return null;
            }

            /* 카카오 로그인 시 */
            if (user.getProvider().equals("kakao")) {
                socialDto = socialService.verificationKakao(user.getKakaoToken());
            }
            /* 페이스북 로그인 시 */
            else if (user.getProvider().equals("facebook")) {
                socialDto = socialService.verificationFacebook(user.getFacebookId(), user.getFacebookToken());
            }

            if (socialDto != null) {
                user.setEmail(socialDto.getEmail().replace("_", "&"));
                user.setPassword(user.getEmail() + env.getProperty("social.secret"));
                if (!userService.isUser(user.getEmail())) {
                    writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.U402, socialDto)));
                    return null;
                }
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    user.getPassword()
            );

            authenticate = authenticationManager.authenticate(authenticationToken);

        } catch (UsernameNotFoundException usernameNotFoundException) {
            writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseGlobalMessage.G400)));
        } catch (Exception e) {
            writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.U400)));
        } finally {
            return authenticate;
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        String refreshKey = UUID.randomUUID().toString();
        userService.saveRefreshToken(principal.getId(), refreshKey);

        String accessToken = jwtTokenProvider.createToken(principal.getId());
        tokenRedisRepository.save(principal.getId(), accessToken);
        String refreshToken = jwtTokenProvider.createToken(principal.getId(), refreshKey);

        Cookie refreshCookie = new Cookie(tokenProperties.getRefresh().getHeader(), refreshToken);

        refreshCookie.setMaxAge(tokenProperties.getRefresh().getExpirationTime().intValue());
        refreshCookie.setSecure(true);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");

        response.addCookie(refreshCookie);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();

        TokenDto tokenInfo = new TokenDto("JWT", accessToken, tokenProperties.getAccess().getExpirationTime(), tokenProperties.getRefresh().getExpirationTime());

        UserProfileTokenDto responseBody = new UserProfileTokenDto(userService.findUserProfile(principal.getUserEntity()), tokenInfo, blockRepository.findUserToIdsByUserFromId(principal.getId()));

        writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseGlobalMessage.G200, responseBody)));
    }

}
