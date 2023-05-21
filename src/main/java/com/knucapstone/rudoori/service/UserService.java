package com.knucapstone.rudoori.service;

import com.knucapstone.rudoori.model.dto.Phw;
import com.knucapstone.rudoori.model.dto.*;
import com.knucapstone.rudoori.model.entity.Mention;
import com.knucapstone.rudoori.model.entity.Score;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.repository.MentionRepository;
import com.knucapstone.rudoori.repository.ScoreRepository;
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
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MentionRepository mentionRepository;
    private final ScoreRepository scoreRepository;
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
        var user = userRepository.findById(request.getUserId()).orElseThrow(()-> new NullPointerException("존재하지 않는 아이디입니다."));

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

        if(findInfo.isEnabled()) {
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

        if (findInfo.isEnabled()) {
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

    @Transactional
    public ScoreResponse scoreForMan(String opponentId, ScoreRequest scoreRequest, UserInfo userinfo) {
        UserInfo opponent = userRepository.findByUserId(opponentId).orElseThrow(() -> new NullPointerException("존재하지 않는 아이디입니다."));
        UserInfo user = userRepository.findByUserId(userinfo.getUserId()).orElseThrow(() -> new NullPointerException("존재하지 않는 아이디입니다."));

        if (!opponentId.equals(user.getUserId())) {

            // opponentIdCount: score db에서 찾은 현재까지의 opponentId의 개수
            long opponentIdCount;

            if (!scoreRepository.findByOpponentId(opponentId).isEmpty()) {
                opponentIdCount = scoreRepository.countByOpponentId(opponentId);
            } else {
                opponentIdCount = 0L;
            }

            // avgGrade(평균값): ((opponentIdCount * 현재까지 opponent의 점수) + 더할 점수) / opponentIdCount + 방금 추가한 개수;
            double avgGrade;

            if (opponent.getScore() != null) {
                avgGrade = ((opponentIdCount * opponent.getScore()) + scoreRequest.getGrade()) / (opponentIdCount + 1);
            } else {
                avgGrade = scoreRequest.getGrade();
            }

            Score saveScore = Score.builder()
                    .userId(user)
                    .opponentId(opponent.getUserId())
                    .grade(scoreRequest.getGrade())
                    .build();

            opponent.setScore(avgGrade);

            scoreRepository.save(saveScore);

            return ScoreResponse.builder()
                    .opponentNickName(opponent.getNickname())
                    .opponentGrade(opponent.getScore())
                    .build();
        } else {
            throw new RuntimeException("자신은 평가할 수 없습니다!");
        }
    }

    @Transactional
    public UserScoreResponse getUserMannerScore(UserInfo userinfo) {

        UserInfo user = userRepository.findByUserId(userinfo.getUserId()).orElseThrow(() -> new NullPointerException("존재하지 않는 아이디입니다."));

        String gradeString;

        Optional<Double> score = Optional.ofNullable(user.getScore());
        double userScore = score.orElse(0.0);

        if (Double.compare(userScore, 4.5) >= 0) {
            gradeString = "A+";
        } else if (Double.compare(userScore, 4.5) < 0 && Double.compare(userScore, 4.0) >= 0) {
            gradeString = "A";
        } else if (Double.compare(userScore, 4.0) < 0 && Double.compare(userScore, 3.5) >= 0) {
            gradeString = "B+";
        } else if (Double.compare(userScore, 3.5) < 0 && Double.compare(userScore, 3.0) >= 0) {
            gradeString = "B";
        } else if (Double.compare(userScore, 3.0) < 0 && Double.compare(userScore, 2.5) >= 0) {
            gradeString = "C+";
        } else if (Double.compare(userScore, 2.5) < 0 && Double.compare(userScore, 2.0) >= 0) {
            gradeString = "C";
        } else if (Double.compare(userScore, 2.0) < 0 && Double.compare(userScore, 1.5) >= 0) {
            gradeString = "D+";
        } else if (Double.compare(userScore, 1.5) < 0 && Double.compare(userScore, 1.0) >= 0) {
            gradeString = "D";
        } else {
            gradeString = "F";
        }


        return UserScoreResponse.builder()
                .nickname(gradeString)
                .gradeString(gradeString)
                .grade(userScore)
                .build();
    }

}
