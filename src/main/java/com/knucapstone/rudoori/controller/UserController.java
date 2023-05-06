package com.knucapstone.rudoori.controller;

import com.knucapstone.rudoori.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


}
