package com.knucapstone.rudoori.service;

import com.knucapstone.rudoori.config.JwtService;
import com.knucapstone.rudoori.model.dto.UserInfoResponse;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public UserInfoResponse getInfo(String userId) {
        UserInfo user = userRepository.findById(userId).orElseThrow(NullPointerException::new);
        return UserInfoResponse
                .builder()
                .userId(user.getUserId())
                .userName(user.getUsername())
                .birthday(user.getBirthday())
                .gender(user.getGender())
                .nickName(user.getNickname())
                .major(user.getMajor())
                .build();
    }
}
