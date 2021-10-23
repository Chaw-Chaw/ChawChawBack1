package okky.team.chawchaw.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "redis")
@Getter @Setter
public class RedisProperties {

    private Block block;
    private Message message;
    private Like like;
    private Views views;
    private ViewDuplex viewDuplex;

    @Getter @Setter
    public static class Block {
        private String prefix;
    }

    @Getter @Setter
    public static class Message {
        private String prefix;
        private Long expirationTime;
    }

    @Getter @Setter
    public static class Like {
        private String prefix;
        private Long expirationTime;
    }

    @Getter @Setter
    public static class Views {
        private String prefix;
        private Long expirationTime;
    }

    @Getter @Setter
    public static class ViewDuplex {
        private String prefix;
        private Long expirationTime;
    }

}
