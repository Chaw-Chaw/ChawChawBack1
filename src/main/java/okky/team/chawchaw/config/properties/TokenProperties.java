package okky.team.chawchaw.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "token")
@Getter @Setter
public class TokenProperties {

    private String secret;
    private String prefix;
    private Access access;
    private Refresh refresh;

    @Getter @Setter
    public static class Access {
        private String header;
        private Long expirationTime;
    }

    @Getter @Setter
    public static class Refresh {
        private String header;
        private Long expirationTime;
    }

}
