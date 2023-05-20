package com.knucapstone.rudoori.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knucapstone.rudoori.config.JwtService;
import com.knucapstone.rudoori.model.dto.Authentication;
import com.knucapstone.rudoori.model.entity.Role;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.repository.UserRepository;
import com.knucapstone.rudoori.token.Token;
import com.knucapstone.rudoori.token.TokenRepository;
import com.knucapstone.rudoori.token.TokenType;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * 로그인 기능
     * 학번이 존재하는지 확인
     * 없으면, DB에 저장 후 토큰 발급
     * @param request
     * @return
     */
    @Transactional
    public Authentication.AuthenticationResponse register(Authentication.RegisterRequest request) {

        Optional<UserInfo> info = userRepository.findById(request.getUserId());

        if(info.isPresent()){
            throw new DuplicateRequestException("중복된 학번입니다.");
        }

        UserInfo user = UserInfo.builder()
                .userId(request.getUserId())
                .name(request.getUserName())
                .birthday(request.getBirthday())
                .gender(request.getGender())
                .email(request.getUserMail())
                .nickname(request.getNickname())
                .major(request.getMajor())
                .password(passwordEncoder.encode(request.getPassword()))
                .userRole(Role.USER)
                .isUsed(true)
                .isBlocked(false)
                .phoneNumber(request.getPhoneNumber())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, refreshToken);
        return Authentication.AuthenticationResponse.builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .build();
    }



    @Transactional
    public Authentication.AuthenticationResponse authenticate(Authentication.AuthenticationRequest request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserId(),
                        request.getPassword()
                )
        );
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new NullPointerException("존재하지 않는 계정입니다."));
        var jwtToken = jwtService.generateToken(user);

        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user,refreshToken);
        return Authentication.AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userId;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userId = jwtService.extractUserId(refreshToken);
        if (userId != null) {
            var user = this.userRepository.findById(userId)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = Authentication.AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
    private void revokeAllUserTokens(UserInfo user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(UserInfo savedUser, String jwtToken) {
        var token = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

}
