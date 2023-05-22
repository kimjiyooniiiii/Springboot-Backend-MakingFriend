package com.knucapstone.rudoori.controller;

import com.knucapstone.rudoori.common.ApiResponse;
import com.knucapstone.rudoori.model.dto.User;
import com.knucapstone.rudoori.model.entity.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.knucapstone.rudoori.model.dto.*;
import com.knucapstone.rudoori.model.dto.UserInfoDto;
import com.knucapstone.rudoori.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PatchMapping("/info/update/{userId}")
    public ApiResponse<User.UserInfoResponse> updateUserInfo
            (@PathVariable("userId")String userId, @RequestBody User.UpdateInfoRequest updateRequest) {
        return ApiResponse.createSuccess(userService.updateUserInfo(userId, updateRequest));
    }

    @PostMapping("/logout")
    public ApiResponse<Boolean> logoutUser(@RequestBody LogoutRequest logoutRequest){
        return ApiResponse.createSuccess(userService.logoutUser(logoutRequest));
    }
    @PostMapping("/write/mention/{opponentId}")
    public ApiResponse<MentionDto.MentionResponse> mentionForMan(@PathVariable("opponentId") String opponentId, @RequestBody MentionDto.MentionRequest mentionRequest) {
        return ApiResponse.createSuccess(userService.mentionForMan(opponentId, mentionRequest));
    }

    @GetMapping("/read/mentions/{userId}")
    public ApiResponse<List<String>> showMentionList(@PathVariable("userId") String userId) {
        return ApiResponse.createSuccess(userService.showMentionList(userId));
    }

    @PostMapping("/block/person")
    public ApiResponse<User.BlockResponse> blockUserId(@RequestBody @Valid User.BlockRequest blockRequest) {
        return ApiResponse.createSuccess(userService.blockUserId(blockRequest));
    }

    @PostMapping("/score/{opponentId}")
    public ApiResponse<ScoreResponse> scoreForMan(@PathVariable("opponentId")String opponentId, @RequestBody ScoreRequest scoreRequest,  @AuthenticationPrincipal UserInfo userinfo){
        return ApiResponse.createSuccess(userService.scoreForMan(opponentId, scoreRequest, userinfo));
    }

    @GetMapping("/info/score")
    public ApiResponse<UserScoreResponse> getUserMannerScore(@AuthenticationPrincipal UserInfo userinfo){
        return ApiResponse.createSuccess(userService.getUserMannerScore(userinfo));
    }
}

