package com.knucapstone.rudoori.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
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
    private String userId;
    private String userName;
    private String birthday;
    private String gender;
    private String major;
    private String nickname;
    private String userMail;
    private String password;
    private String phoneNumber;
}
