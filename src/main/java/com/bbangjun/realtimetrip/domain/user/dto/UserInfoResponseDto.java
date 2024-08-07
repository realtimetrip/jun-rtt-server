package com.bbangjun.realtimetrip.domain.user.dto;

import com.bbangjun.realtimetrip.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {
    private Long userId;
    private String email;
    private String nickName;
    private String profile;

    public static UserInfoResponseDto toUserInfoResponseDto(User user){
        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto();

        userInfoResponseDto.setEmail(user.getEmail());
        userInfoResponseDto.setUserId(user.getUserId());
        userInfoResponseDto.setNickName(user.getNickname());
        userInfoResponseDto.setProfile(user.getProfile());

        return userInfoResponseDto;
    }
}
