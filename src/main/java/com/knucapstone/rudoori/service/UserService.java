package com.knucapstone.rudoori.service;

import com.knucapstone.rudoori.config.JwtService;
import com.knucapstone.rudoori.model.dto.UserInfoDto;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public boolean deleteUser(UserInfoDto loginInfo) {
        String userId = loginInfo.getUserId();
        String pwd = loginInfo.getPassword();
        boolean equalPwd = false;
        UserInfo userInfo = userRepository.findByUserId(userId).orElseThrow(() -> new NullPointerException("존재하지 않는 아이디입니다."));
        if (userInfo.isEnabled() && userInfo != null) {
            String storedPwd = userInfo.getPassword();
            equalPwd = passwordEncoder.matches(pwd, storedPwd);
            if (equalPwd) {
                userRepository.deleteById(userId);
            }
            return equalPwd;
        }
        return equalPwd;

    }

    @Transactional
    public boolean updatePwd(UserInfoDto updatePwdInfo) {
        String userId = updatePwdInfo.getUserId();
        String pwd = updatePwdInfo.getPassword();
        String updatedPwd = updatePwdInfo.getUpdatedPwd();
        boolean equalPwd = false;
        UserInfo userInfo = userRepository.findByUserId(userId).orElseThrow(() -> new NullPointerException("존재하지 않는 아이디입니다."));
        if (userInfo.isEnabled() && userInfo != null) {
            String storedPwd = userInfo.getPassword();
            equalPwd = passwordEncoder.matches(pwd, storedPwd);
            if (equalPwd) {
                userInfo.setPassword(passwordEncoder.encode(updatedPwd));
            }
            return equalPwd;
        }
        return equalPwd;
    }

    @Transactional(readOnly = true)
    public UserInfoDto getUserProfile(String userId) {
        UserInfo userInfo = userRepository.findByUserId(userId).orElseThrow(() -> new NullPointerException("존재하지 않는 아이디입니다."));
        if (userInfo.isEnabled() && userInfo != null) {

            UserInfoDto userProfile = UserInfoDto.builder()
                    .major(userInfo.getMajor())
                    .nickname(userInfo.getNickname())
                    .score(userInfo.getScore())
                    .build();
            return userProfile;
        }
        return null;
    }
}
