package pothole_solution.core.infra.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pothole_solution.core.infra.config.filter.SessionAccessDeniedHandler;
import pothole_solution.core.infra.config.filter.SessionAuthenticationEntryPoint;
import pothole_solution.core.infra.config.filter.SessionAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SessionAuthenticationFilter sessionAuthenticationFilter;
    private final SessionAuthenticationEntryPoint sessionAuthenticationEntryPoint;
    private final SessionAccessDeniedHandler sessionAccessDeniedHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // 웹앱이기 때문에 불필요
                .requestCache(RequestCacheConfigurer::disable) // 웹앱이기 때문에 불필
                .sessionManagement((sessionManagement) -> sessionManagement
                        .maximumSessions(2)
                )
                .authorizeHttpRequests(authorizeRequest -> authorizeRequest
                        .requestMatchers( // 인증 예외 처리
                                AntPathRequestMatcher.antMatcher("/pothole/v1/auth/**/join"),
                                AntPathRequestMatcher.antMatcher("/pothole/v1/auth/**/login")).permitAll()
                        .requestMatchers( // 포트홀 탐지 차량 예외 처리
                                AntPathRequestMatcher.antMatcher("/pothole/v1/manager/detection/start")).permitAll()
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/pothole/v1/manager/**")).hasRole("MANAGER")
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/pothole/v1/worker/**")).hasRole("WORKER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(sessionAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(sessionAuthenticationEntryPoint)
                        .accessDeniedHandler(sessionAccessDeniedHandler)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
