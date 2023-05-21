package com.knucapstone.rudoori.model.dto;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private String userId;
    private String userName;
    private String birthday;
    private String gender;
    private String major;
    private String userMail;
    private String password;
    private String phoneNumber;
    private String updatedPwd;
    private Double score;
    private String image;
    private String nickname;

    @Builder
    public UserInfoDto(String major, String nickname, Double score) {
        this.major = major;
        this.nickname = nickname;
        this.score = score;
    }
}
