package com.knucapstone.rudoori.controller;


import com.knucapstone.rudoori.model.dto.MentionDto;
import com.knucapstone.rudoori.model.dto.Phw;
import com.knucapstone.rudoori.model.entity.Mention;
import com.knucapstone.rudoori.model.entity.UserInfo;
import com.knucapstone.rudoori.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.knucapstone.rudoori.common.ApiResponse;
import com.knucapstone.rudoori.model.dto.UserInfoResponse;
import com.knucapstone.rudoori.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @DeleteMapping("/user/quit")
    public boolean deleteUser(@RequestBody Phw.LoginInfo LoginInfo){
        return userService.deleteUser(LoginInfo);
    }

    @GetMapping("/info")
    public ApiResponse<UserInfoResponse> getUserInfo(@RequestParam String userId){
        return ApiResponse.createSuccess(userService.getInfo(userId));
    }


    @PatchMapping("/user/pwd")
    public boolean updatePwd(@RequestBody Phw.UpdatePwdInfo updatePwdInfo){
        return userService.updatePwd(updatePwdInfo);
    }

    @GetMapping("/profile/{userId}")
    public Phw.UserProfile getUserProfile(@PathVariable("userId")String userId) {
        return userService.getUserProfile(userId);
    }

    @PostMapping("/user/score/mention/{yourId}")
    public ResponseEntity<MentionDto.MentionResponse> mentionForMan(@PathVariable("yourId")String yourId, @RequestBody MentionDto.MentionRequest mentionRequest) {
        return ResponseEntity.ok(userService.mentionForMan(yourId, mentionRequest));
    }
}

