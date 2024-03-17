package com.bbangjun.realtimetrip.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public String test(){
        return "Hello";
    }
}
