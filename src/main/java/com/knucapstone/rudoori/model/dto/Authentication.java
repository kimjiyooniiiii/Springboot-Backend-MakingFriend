package com.knucapstone.rudoori.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
public class Authentication {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthenticationRequest {
        @NotNull
        private String userId;
        @NotNull
        private String password;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class AuthenticationResponse {
        private String accessToken;
        private String refreshToken;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class RegisterRequest {
        //    {
//            "userId" : "201413373",
//            "userName" : "김동훈",
//            "birthday" : "1995.06.15",
//            "gender" : "Male",
//            "major" : "컴퓨터공학과",
//            "nickname" : "푸들",
//            "userMail" : "a@gmail.com",
//            "phoneNumber" : "010-1111-1111",
//            "password" : "1234"
//    }
        @Pattern(regexp = "^20\\d{7}$", message = "9자리의 숫자만 입력가능합니다")
        private String userId;
        @NotNull
        private String userName;
        @NotNull
        private String birthday;
        @NotNull
        private String gender;
        @NotNull
        private String major;
        @NotNull
        private String nickname;
        @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "이메일 형식이 맞지않습니다.")
        private String userMail;
        @NotNull
        private String password;
        @Pattern(regexp = "^01([0|1|6|7|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "휴대폰 번호가 형식에 맞지 않습니다.")
        private String phoneNumber;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterResponse {
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


}
