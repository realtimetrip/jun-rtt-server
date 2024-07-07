package com.bbangjun.realtimetrip.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    NOT_FOUND(400, HttpStatus.NOT_FOUND, "Not Found"),
    OK(200, HttpStatus.OK, "ok"),
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "internal server error"),

    // verificationCode
    INCORRECT_VERIFICATIONCODE(1001, HttpStatus.NOT_FOUND, "인증번호가 틀렸거나 존재하지 않습니다."),

    // user
    INCORRECT_LOGIN_INFO(2001, HttpStatus.NOT_FOUND, "이메일 혹은 비밀번호가 틀렸습니다."),

    // country
    COUNTRY_ALREADY_EXIST(3001, HttpStatus.INTERNAL_SERVER_ERROR, "이미 해당 나라(채팅방)가 존재합니다."),

    // chat
    NO_ENTER_TYPE(4001, HttpStatus.INTERNAL_SERVER_ERROR, "채팅 타입이 입장(ENTER)이 아닙니다."),
    NO_TALK_TYPE(4002, HttpStatus.INTERNAL_SERVER_ERROR, "채팅 타입이 대화(TALK)이 아닙니다."),

    // chatRoom
    NO_CHATROOM_EXIST(5001, HttpStatus.NOT_FOUND, "채팅방이 존재하지 않습니다.");

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
