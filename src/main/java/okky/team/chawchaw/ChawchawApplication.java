package okky.team.chawchaw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ChawchawApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChawchawApplication.class, args);
    }

}
