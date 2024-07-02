package com.bbangjun.realtimetrip.domain.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bbangjun.realtimetrip.domain.user.dto.SignUpRequestDto;
import com.bbangjun.realtimetrip.domain.user.dto.UserDto;
import com.bbangjun.realtimetrip.domain.user.repository.UserRepository;
import com.bbangjun.realtimetrip.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public UserDto signUp(SignUpRequestDto signUpRequestDto) throws IOException {
        String profileUrl = uploadProfileImageToS3(signUpRequestDto.getProfile());

        User user = User.builder()
                .email(signUpRequestDto.getEmail())
                .password(signUpRequestDto.getPassword())
                .nickname(signUpRequestDto.getNickName())
                .profile(profileUrl)
                .build();

        userRepository.save(user);

        return UserDto.toUserDto(userRepository.findByEmail(signUpRequestDto.getEmail()));
    }

    private String uploadProfileImageToS3(MultipartFile profile) throws IOException {
        String fileName = UUID.randomUUID() + "-" + profile.getOriginalFilename();
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, profile.getInputStream(), null));
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public UserDto authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        return UserDto.toUserDto(user);
    }

}
