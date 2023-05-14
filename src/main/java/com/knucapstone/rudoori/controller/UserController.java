package com.knucapstone.rudoori.controller;


import com.knucapstone.rudoori.model.dto.Phw;
import com.knucapstone.rudoori.model.dto.LogoutRequest;
import com.knucapstone.rudoori.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.knucapstone.rudoori.common.ApiResponse;
import com.knucapstone.rudoori.model.dto.UserInfoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @DeleteMapping("/quit")
    public boolean deleteUser(@RequestBody Phw.LoginInfo LoginInfo){
        return userService.deleteUser(LoginInfo);
    }

    @GetMapping("/info")
    public ApiResponse<UserInfoResponse> getUserInfo(@RequestParam String userId){
        return ApiResponse.createSuccess(userService.getInfo(userId));
    }



    @PatchMapping("/pwd")
    public boolean updatePwd(@RequestBody Phw.UpdatePwdInfo updatePwdInfo){
        return userService.updatePwd(updatePwdInfo);
    }

    @GetMapping("/profile/{userId}")
    public Phw.UserProfile getUserProfile(@PathVariable("userId")String userId) {
        return userService.getUserProfile(userId);
    }


    @PostMapping("/logout")
    public ApiResponse logoutUser(@RequestBody LogoutRequest logoutRequest){
        return ApiResponse.createSuccess(userService.logoutUser(logoutRequest));
    }

}

