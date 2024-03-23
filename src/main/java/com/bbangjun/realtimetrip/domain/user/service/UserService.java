package com.bbangjun.realtimetrip.domain.user.service;

import com.bbangjun.realtimetrip.domain.user.dto.UserDto;
import com.bbangjun.realtimetrip.domain.user.repository.UserRepository;
import com.bbangjun.realtimetrip.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserDto signUp(UserDto userDto) {

        User user = User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .nickname(userDto.getNickname())
                .build();

        userRepository.save(user);

        return UserDto.toUserDto(userRepository.findByEmail(userDto.getEmail()));
    }

    public UserDto authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        return UserDto.toUserDto(user);
    }
}
