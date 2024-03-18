package com.bbangjun.realtimetrip.user.controller;

import com.bbangjun.realtimetrip.user.dto.UserDto;
import com.bbangjun.realtimetrip.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @PostMapping("/signup")
    public UserDto signUp(@RequestBody UserDto userDto) {

        return userService.signUp(userDto);
    }
}
