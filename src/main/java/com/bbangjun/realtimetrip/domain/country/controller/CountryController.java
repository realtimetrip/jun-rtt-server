package com.bbangjun.realtimetrip.domain.country.controller;

import com.bbangjun.realtimetrip.domain.country.service.CountryService;
import com.bbangjun.realtimetrip.global.response.BaseResponse;
import com.bbangjun.realtimetrip.global.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "CountryController", description = "나라 관련 API")
public class CountryController {
    private final CountryService countryService;

    // API: 국가별 채팅방 생성
    @PostMapping("/create-chatroom")
    @Operation(summary = "국가별 채팅방 생성", description = "국가 정보와 해당 국가들에 대한 채팅방을 생성합니다.")
    public BaseResponse<Object> createChatRoom(){
        try{
            if(countryService.createChatRoom())
                return new BaseResponse<>();
            else
                return new BaseResponse<>(ResponseCode.COUNTRY_ALREADY_EXIST);
        }catch (Exception e){
            return new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
