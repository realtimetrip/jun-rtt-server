package com.bbangjun.realtimetrip.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    OK(200, HttpStatus.OK, "ok"),
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "internal server error"),

    // verificationCode
    INCORRECT_VERIFICATIONCODE(1001, HttpStatus.NOT_FOUND, "인증번호가 틀렸습니다."),

    // user
    INCORRECT_LOGIN_INFO(2001, HttpStatus.NOT_FOUND, "이메일 혹은 비밀번호가 틀렸습니다.");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

}
