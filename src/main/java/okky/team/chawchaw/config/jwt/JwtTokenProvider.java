package okky.team.chawchaw.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.config.properties.TokenProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final TokenProperties tokenProperties;

    public String createToken(Long userId) {
        return JWT.create()
                .withSubject("AccessToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenProperties.getAccess().getExpirationTime()))
                .withClaim("userId", userId)
                .sign(Algorithm.HMAC512(tokenProperties.getSecret()));
    }

    public String createToken(Long userId, String refreshKey) {
        return JWT.create()
                .withSubject("RefreshToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenProperties.getRefresh().getExpirationTime()))
                .withClaim("userId", userId)
                .withClaim("refreshKey", refreshKey)
                .sign(Algorithm.HMAC512(tokenProperties.getSecret()));
    }

    public Claim getClaimByTokenAndKey(String token, String key) {
        return JWT.require(Algorithm.HMAC512(tokenProperties.getSecret())).build().verify(token).getClaim(key);
    }

}
