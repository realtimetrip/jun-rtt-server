package com.bbangjun.realtimetrip.domain.authnum.dto;

import com.bbangjun.realtimetrip.domain.authnum.entity.AuthNum;
import com.bbangjun.realtimetrip.domain.user.dto.UserDto;
import com.bbangjun.realtimetrip.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthNumDto {

    private String email;

    private String authNum;

    private LocalDateTime created_at;

    public static AuthNumDto toAuthNumDto(AuthNum authNum){
        AuthNumDto authNumDto = new AuthNumDto();

        authNumDto.setEmail(authNum.getEmail());
        authNumDto.setAuthNum(authNum.getAuthNum());
        authNumDto.setCreated_at(authNum.getCreated_at());

        return authNumDto;
    }
}
