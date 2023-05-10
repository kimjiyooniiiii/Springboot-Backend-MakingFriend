package com.knucapstone.rudoori.model.dto;

import com.knucapstone.rudoori.model.entity.UserInfo;
import lombok.*;

@Getter
@Setter
public class Phw {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginInfo {
        private String userId;
        private String password;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdatePwdInfo {
        private String userId;
        private String password;
        private String updatedPwd;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserProfile{
        private String major;
        private String nickname;
        private Double score;
        private String image;

        @Builder
        public UserProfile(String major, String nickname, Double score) {
            this.major = major;
            this.nickname = nickname;
            this.score = score;
        }
    }

}
