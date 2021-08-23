package okky.team.chawchaw.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okky.team.chawchaw.config.auth.PrincipalDetails;
import okky.team.chawchaw.user.UserEntity;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.utils.dto.DefaultResponseVo;
import okky.team.chawchaw.utils.message.ResponseAuthMessage;
import okky.team.chawchaw.utils.message.ResponseUserMessage;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private Environment env;
    private UserRepository userRepository;
    private ObjectMapper mapper = new ObjectMapper();

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, Environment env, UserRepository userRepository) {
        super(authenticationManager);
        this.env = env;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        PrintWriter writer = null;

        try {

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            String jwtHeader = request.getHeader("Authorization");

            if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
                chain.doFilter(request, response);
                return;
            }

            String token = jwtHeader.replace("Bearer ", "");
            String email = JWT.require(Algorithm.HMAC512(env.getProperty("token.secret")))
                    .build()
                    .verify(token)
                    .getClaim("email")
                    .asString();

            if (email == null) {
                throw new JWTDecodeException("JWT 포맷 오류");
            }

            UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("유저를 찾지 못함"));
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);

        } catch (TokenExpiredException tokenExpiredException) {
            response.setStatus(401);
            writer = response.getWriter();
            writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseAuthMessage.EXPIRE_ACCESS_TOKEN, false)));

        } catch (JWTDecodeException | SignatureVerificationException verificationException) {
            response.setStatus(401);
            writer = response.getWriter();
            writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseAuthMessage.WRONG_ACCESS_TOKEN_FORM, false)));

        } catch (UsernameNotFoundException usernameNotFoundException) {
            response.setStatus(401);
            writer = response.getWriter();
            writer.print(mapper.writeValueAsString(DefaultResponseVo.res(ResponseUserMessage.ID_NOT_EXIST, false)));

        }
    }
}