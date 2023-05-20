package com.knucapstone.rudoori.controller;


import com.knucapstone.rudoori.common.ApiResponse;
import com.knucapstone.rudoori.model.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.knucapstone.rudoori.model.dto.*;
import com.knucapstone.rudoori.model.entity.Mention;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.model.dto.UserInfoDto;
import com.knucapstone.rudoori.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.knucapstone.rudoori.common.ApiResponse;
import com.knucapstone.rudoori.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @DeleteMapping("/quit")
    public boolean deleteUser(@RequestBody UserInfoDto loginInfo) {
        return userService.deleteUser(loginInfo);
    }

    @PatchMapping("/pwd")
    public boolean updatePwd(@RequestBody UserInfoDto updatePwdInfo) {
        return userService.updatePwd(updatePwdInfo);
    }

    @GetMapping("/profile/{userId}")
    public UserInfoDto getUserProfile(@PathVariable("userId") String userId) {
        return userService.getUserProfile(userId);
    }

    @GetMapping("/info")
    public ApiResponse<User.UserInfoResponse> getUserInfo(@RequestParam String userId) {
        return ApiResponse.createSuccess(userService.getInfo(userId));
    }

    @PostMapping("/logout")
    public ApiResponse logoutUser(@RequestBody LogoutRequest logoutRequest) {
        return ApiResponse.createSuccess(userService.logoutUser(logoutRequest));
    }

    @PostMapping("/write/mention/{opponentId}")
    public ApiResponse<MentionResponse> mentionForMan(@PathVariable("opponentId") String opponentId, @RequestBody MentionRequest mentionRequest) {
        return ApiResponse.createSuccess(userService.mentionForMan(opponentId, mentionRequest));
    }

    @GetMapping("/read/mentions/{userId}")
    public ApiResponse<List<String>> showMentionList(@PathVariable("userId") String userId) {
        return ApiResponse.createSuccess(userService.showMentionList(userId));
    }


}

