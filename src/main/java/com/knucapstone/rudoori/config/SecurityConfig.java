package com.knucapstone.rudoori.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.knucapstone.rudoori.model.entity.Role.USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    // HTTP 보안 구성을 위한 SecurityFilterChain 빈을 생성.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable() // CSRF 공격 방지 비활성화

                .authorizeHttpRequests()
                .requestMatchers("/auth/**", "/user/**")
                .permitAll() // 빈 문자열("")에 대한 요청은 모든 사용자에게 허용

                .anyRequest()
                .authenticated() // 그 외의 요청은 인증된 사용자만 허용

                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 생성 비활성화

                .and()

                .authenticationProvider(authenticationProvider) // AuthenticationProvider 등록

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 이전에 추가

        return http.build();
    }
}
