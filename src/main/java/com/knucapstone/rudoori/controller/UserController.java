package com.knucapstone.rudoori.controller;


import com.knucapstone.rudoori.common.ApiResponse;
import com.knucapstone.rudoori.model.dto.Phw;
import com.knucapstone.rudoori.model.dto.User;
import com.knucapstone.rudoori.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


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
    public ApiResponse<User.UserInfoResponse> getUserInfo(@RequestParam String userId){
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
}

