package com.knucapstone.rudoori.service;

import com.knucapstone.rudoori.config.JwtService;
import com.knucapstone.rudoori.model.dto.MentionDto;
import com.knucapstone.rudoori.model.dto.Phw;
import com.knucapstone.rudoori.model.dto.UserInfoResponse;
import com.knucapstone.rudoori.model.entity.Mention;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.repository.MentionRepository;
import com.knucapstone.rudoori.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MentionRepository mentionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public boolean deleteUser(Phw.LoginInfo loginInfo) {
        String id = loginInfo.getUserId();
        String pwd = loginInfo.getPassword();

        UserInfo userInfo = userRepository.findByUserId(id).get();
        String storedPwd = userInfo.getPassword();
        boolean equalPwd = passwordEncoder.matches(pwd, storedPwd);
        if (equalPwd) {
            userRepository.deleteById(id);
        }
        return equalPwd;
    }

    @Transactional
    public boolean updatePwd(Phw.UpdatePwdInfo updatePwdInfo) {
        String id = updatePwdInfo.getUserId();
        String pwd = updatePwdInfo.getPassword();
        String updatedPwd = updatePwdInfo.getUpdatedPwd();
        UserInfo userInfo = userRepository.findByUserId(id).get();
        String storedPwd = userInfo.getPassword();
        boolean equalPwd = passwordEncoder.matches(pwd, storedPwd);
        if (equalPwd) {
            userInfo.setPassword(passwordEncoder.encode(updatedPwd));
        }
        return equalPwd;
    }
    @Transactional(readOnly = true)
    public Phw.UserProfile getUserProfile(String userId) {
        UserInfo userInfo = userRepository.findByUserId(userId).get();

        Phw.UserProfile userProfile = Phw.UserProfile.builder()
                .major(userInfo.getMajor())
                .nickname(userInfo.getNickname())
                .score(userInfo.getScore())
                .build();
        return userProfile;
    }

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

        @Transactional
    public MentionDto.MentionResponse mentionForMan(String yourId, MentionDto.MentionRequest mentionRequest) {
        UserInfo findInfo = userRepository.findByUserId(yourId).get();

        if(findInfo.isEnabled() && findInfo != null) {
            Mention newM = new Mention(null, findInfo, mentionRequest.getContent());
            mentionRepository.save(newM);

            MentionDto.MentionResponse mentionResponse = MentionDto.MentionResponse.builder()
                            .userId(yourId)
                            .content(mentionRequest.getContent())
                            .build();

            return mentionResponse;
        }

        return null;
    }
}
