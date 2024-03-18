package com.bbangjun.realtimetrip.user.dto;

import com.bbangjun.realtimetrip.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String profile;

    public static UserDto toUserDto(User user){
        UserDto userDto = new UserDto();

        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setUserId(user.getUserId());
        userDto.setNickname(user.getNickname());

        return userDto;
    }
}
