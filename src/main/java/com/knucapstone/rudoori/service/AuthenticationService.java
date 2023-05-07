package com.knucapstone.rudoori.service;

import com.knucapstone.rudoori.config.JwtService;
import com.knucapstone.rudoori.model.dto.AuthenticationRequest;
import com.knucapstone.rudoori.model.dto.AuthenticationResponse;
import com.knucapstone.rudoori.model.dto.RegisterRequest;
import com.knucapstone.rudoori.model.entity.Role;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {

        UserInfo info = userRepository.findById(request.getUserId()).orElseThrow(NullPointerException::new);

        if(info != null){
            throw new DuplicateRequestException();
        }

        UserInfo user = UserInfo.builder()
                .userId(request.getUserId())
                .userName(request.getUserName())
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
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .build();
    }

    @Transactional(readOnly = true)
    public AuthenticationResponse authenticate(AuthenticationRequest request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserId(),
                        request.getPassword()
                )
        );
        var user = userRepository.findById(request.getUserId())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
