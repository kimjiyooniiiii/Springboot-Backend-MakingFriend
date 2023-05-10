package com.knucapstone.rudoori.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터(JwtAuthenticationFilter)입니다.
 * 해당 필터는 Spring Security의 OncePerRequestFilter를 상속하고 있으며, HTTP 요청마다 한 번만 실행되도록 보장합니다.
 *
 * 주요 역할은 다음과 같습니다:
 *
 * HTTP 요청의 Authorization 헤더에서 JWT 토큰을 추출합니다.
 * 추출한 토큰을 이용하여 사용자 인증을 수행합니다.
 * JWT 토큰이 유효하면 사용자를 인증하고, SecurityContextHolder에 인증 정보를 설정합니다.
 * 인증이 필요한 요청에 대해서는 해당 요청에 필요한 권한을 가진 사용자만 접근할 수 있도록 설정합니다.
 * 이 필터는 Spring Security의 인증 과정 중에서 JWT 토큰을 활용하여 사용자를 인증하는 역할을 담당합니다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userId;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        jwt =authHeader.substring(7);
//        if(jwtService.isTokenExpired(jwt)){
//            throw new RuntimeException("기간이 안됨");
//        }
        userId = jwtService.extractUserId(jwt);
        /*SecurityContextHolder.getContext().getAuthentication()==null
        인증이 null이면 사용자가 아직 인증을 하지 않았음을 의미
        사용자가 아직 연결되지 않았음을 의미
         */

        if(userId != null && SecurityContextHolder.getContext().getAuthentication()==null){

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
            if(jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
