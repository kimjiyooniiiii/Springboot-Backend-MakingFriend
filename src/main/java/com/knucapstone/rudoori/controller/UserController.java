package com.knucapstone.rudoori.controller;

import com.knucapstone.rudoori.model.dto.UserInfoDto;
import com.knucapstone.rudoori.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @DeleteMapping("/user/quit")
    public boolean deleteUser(@RequestBody UserInfoDto loginInfo){
        return userService.deleteUser(loginInfo);
    }

    @PatchMapping("/user/pwd")
    public boolean updatePwd(@RequestBody UserInfoDto updatePwdInfo){
        return userService.updatePwd(updatePwdInfo);
    }

    @GetMapping("/profile/{userId}")
    public UserInfoDto getUserProfile(@PathVariable("userId")String userId) {
        return userService.getUserProfile(userId);
    }
}

