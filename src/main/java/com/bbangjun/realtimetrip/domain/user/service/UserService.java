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
        log.info("userDto email = {}", userDto.getEmail());
        log.info("userDto password = {}", userDto.getPassword());
        log.info("userDto nickname = {}", userDto.getNickname());
        User user = User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .nickname(userDto.getNickname())
                .build();

        log.info("user email = {}", user.getEmail());
        log.info("user password = {}", user.getPassword());
        log.info("user nickname = {}", user.getNickname());

        userRepository.save(user);

        log.info("save user email = {}", userRepository.findByEmail(userDto.getEmail()));
        log.info("save user password = {}", userRepository.findByEmail(userDto.getPassword()));
        log.info("save user nickname = {}", userRepository.findByEmail(userDto.getNickname()));

        return UserDto.toUserDto(userRepository.findByEmail(userDto.getEmail()));
    }
}
