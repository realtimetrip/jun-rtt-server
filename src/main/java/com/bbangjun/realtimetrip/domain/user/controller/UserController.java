package com.bbangjun.realtimetrip.domain.user.controller;

import com.bbangjun.realtimetrip.domain.user.dto.UserDto;
import com.bbangjun.realtimetrip.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public UserDto signUp(@RequestBody UserDto userDto) {

        return userService.signUp(userDto);
    }
}
