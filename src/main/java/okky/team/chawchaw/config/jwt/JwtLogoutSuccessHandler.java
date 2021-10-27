package okky.team.chawchaw.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import okky.team.chawchaw.user.UserService;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseGlobalMessage;
import okky.team.chawchaw.utils.message.ResponseUserMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private TokenRedisRepository tokenRedisRepository;

    public JwtLogoutSuccessHandler(UserService userService, JwtTokenProvider jwtTokenProvider, TokenRedisRepository tokenRedisRepository) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenRedisRepository = tokenRedisRepository;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        String jwtHeader = request.getHeader("Authorization");

        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            response.setStatus(400);
            writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.U403)));
            return;
        }

        try {
            String token = jwtHeader.replace("Bearer ", "");
            Long userId = jwtTokenProvider.getClaimByTokenAndKey(token, "userId").asLong();
            if (tokenRedisRepository.findByUserId(userId).equals(token)) {
                userService.deleteAccessToken(userId);
                userService.deleteRefreshToken(userId);
            } else {
                response.setStatus(401);
                writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseGlobalMessage.G404)));
                return;
            }

        } catch (Exception e) {
            response.setStatus(400);
            writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.U403)));
            return;
        }

        writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseGlobalMessage.G200)));

    }

}
