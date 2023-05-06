package com.knucapstone.rudoori.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
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
    private String password;
    private String phoneNumber;
}
