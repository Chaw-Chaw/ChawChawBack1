package okky.team.chawchaw.config;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.block.BlockRepository;
import okky.team.chawchaw.config.jwt.*;
import okky.team.chawchaw.config.properties.TokenProperties;
import okky.team.chawchaw.social.SocialService;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Environment env;
    private final UserRepository userRepository;
    private final UserService userService;
    private final SocialService socialService;
    private final TokenProperties tokenProperties;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRedisRepository tokenRedisRepository;
    private final BlockRepository blockRepository;
    @Value("${front.domain}")
    private String frontDomain;
    @Value("${front.domain-local}")
    private String frontDomainLocal;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter())
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), userService, socialService, env, tokenProperties, jwtTokenProvider, tokenRedisRepository, blockRepository))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository, jwtTokenProvider, tokenRedisRepository))
                .formLogin().disable()
                .httpBasic().disable()
                .logout().logoutSuccessHandler(new JwtLogoutSuccessHandler(userService, jwtTokenProvider, tokenRedisRepository)).and()
                .authorizeRequests()
                .antMatchers("/users/signup/**").permitAll()
                .antMatchers("/users/login/**").permitAll()
                .antMatchers("/users/email/duplicate/*").permitAll()
                .antMatchers("/users/auth/**").permitAll()
                .antMatchers("/mail/**").permitAll()
                .antMatchers("/chat/**").access("hasRole('ROLE_USER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/users/rank/**").permitAll()
                .antMatchers("/ws/**").permitAll()
                .anyRequest()
                .access("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')");
    }

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin(frontDomain);
        configuration.addAllowedOrigin(frontDomainLocal);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}
