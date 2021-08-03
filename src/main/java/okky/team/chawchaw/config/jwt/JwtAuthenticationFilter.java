package okky.team.chawchaw.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserService;
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
    private Environment env;
    private UserService userService;
    private JSONObject jsonObject = new JSONObject();
    private ObjectMapper mapper = new ObjectMapper();


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, Environment env, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.env = env;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        PrintWriter writer = null;
        Authentication authenticate = null;

        try {

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            writer = response.getWriter();

            UserEntity user = mapper.readValue(request.getInputStream(), UserEntity.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    user.getPassword()
            );

            authenticate = authenticationManager.authenticate(authenticationToken);

        } catch (UsernameNotFoundException usernameNotFoundException) {
            jsonObject.put("responseMessage", ResponseUserMessage.ID_NOT_EXIST);
            jsonObject.put("isSuccess", false);
            writer.print(jsonObject);
        } catch (Exception e) {
            jsonObject.put("responseMessage", ResponseUserMessage.LOGIN_FAIL);
            jsonObject.put("isSuccess", false);
            writer.print(jsonObject);
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
        jsonObject.put("responseMessage", ResponseUserMessage.LOGIN_SUCCESS);
        jsonObject.put("isSuccess", true);
        writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.LOGIN_SUCCESS, true, userService.findUserProfile(principal.getId()))));
    }
}
