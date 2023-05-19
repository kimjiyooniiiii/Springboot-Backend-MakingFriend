package com.knucapstone.rudoori.service;

import com.knucapstone.rudoori.model.dto.*;
import com.knucapstone.rudoori.model.entity.Mention;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.repository.MentionRepository;
import com.knucapstone.rudoori.repository.UserRepository;
import com.knucapstone.rudoori.token.Token;
import com.knucapstone.rudoori.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MentionRepository mentionRepository;
    private final PasswordEncoder passwordEncoder;


    private final TokenRepository tokenRepository;

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
    @Transactional
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
    public boolean logoutUser(LogoutRequest request) {
        var user = userRepository.findById(request.getUserId()).orElseThrow();

        revokeAllUserTokens(user);

        return true;
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

    @Transactional
    public MentionResponse mentionForMan(String opponentId, MentionRequest mentionRequest) {
        UserInfo findInfo = userRepository.findByUserId(opponentId).orElseThrow(()-> new NullPointerException("존재하지 않는 아이디입니다."));

        if(findInfo.isEnabled() && findInfo != null) {
            Mention newMention = Mention.builder()
                    .userId(findInfo)
                    .content(mentionRequest.getContent())
                    .build();

            mentionRepository.save(newMention);

            return MentionResponse.builder()
                    .opponentNickName(findInfo.getNickname())
                    .content(mentionRequest.getContent())
                    .build();
        }

        return null;
    }

    public List<String> showMentionList(String userId) {
        UserInfo findInfo = userRepository.findByUserId(userId).orElseThrow(() -> new NullPointerException("존재하지 않는 아이디입니다."));

        if (findInfo.isEnabled() && findInfo != null) {
            List<Mention> mentions = mentionRepository.findAllByUserId(findInfo);
            List<String> contents = new ArrayList<>();

            if (!mentions.isEmpty()) {
                for (Mention mention : mentions) {
                    contents.add(mention.getContent());
                }

                return contents;
            }
        }
        return null;
    }
}
