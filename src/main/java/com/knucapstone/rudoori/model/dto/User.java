package com.knucapstone.rudoori.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

public class User {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoResponse {
        //{
        //  "userId" : "201413373",
        //  "userName" : "김동훈",
        //  "birthday" : "1995.06.15",
        //  "gender" : "Male",
        //  "major" : "컴퓨터공학과",
        //  "nickName" : "푸들",
        //  "userMail" : "a@gmail.com",
        //  "password" : "1234"
        //}
        private String userId;
        private String userName;
        private String birthday;
        private String gender;
        private String major;
        private String nickName;
        private String userMail;
        private String phoneNumber;
    }

    @Getter
    @Setter
    public static class BlockRequest{
        @NotNull
        private String userId;
        @NotNull
        private String blockedId;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BlockResponse{
        @NotNull
        private String blockedId;
    }
}
