package com.bbangjun.realtimetrip.domain.authnum.service;

import com.bbangjun.realtimetrip.domain.authnum.dto.AuthNumDto;
import com.bbangjun.realtimetrip.domain.authnum.entity.AuthNum;
import com.bbangjun.realtimetrip.domain.authnum.repository.AuthNumRepository;
import com.bbangjun.realtimetrip.domain.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthNumService {

    // 실제 메일 전송을 위한 send() 메서드 사용
    private final JavaMailSender emailSender;
    private String newAuthNum;

    // 타임리프를 이용한 context 설정
    private final SpringTemplateEngine templateEngine;
    // DB 저장 방식
    private final AuthNumRepository authNumRepository;
    // redis 방식
    private final StringRedisTemplate stringRedisTemplate;


    // 실제 메일 전송 - controller에서 호출
    public void sendEmail(String toEmail) throws MessagingException, UnsupportedEncodingException {

        // 메일 전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(toEmail);
        // 실제 메일 전송
        emailSender.send(emailForm);
    }

    public void sendEmailRedis(String toEmail) throws MessagingException, UnsupportedEncodingException {

        // 메일 전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailFormRedis(toEmail);
        createCodeRedis(toEmail);
        // 실제 메일 전송
        emailSender.send(emailForm);
    }

    // 랜덤 인증 코드 생성
    public void createCode(String email) {
        Random random = new Random();
        newAuthNum = String.valueOf(random.nextInt(9000)+1000); // 범위: 1000 ~ 9999

        AuthNum authNum = AuthNum.builder()
                .authNum(newAuthNum)
                .created_at(LocalDateTime.now())
                .email(email).build();

        authNumRepository.save(authNum);

    }

    // 인증 번호 생성 및 Redis 저장
    private void createCodeRedis(String email) {
        Random random = new Random();
        newAuthNum = String.valueOf(random.nextInt(9000) + 1000); // 1000 ~ 9999 범위의 인증 번호 생성

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(email, newAuthNum, 5, TimeUnit.MINUTES); // Redis에 이메일을 키로, 인증 번호를 값으로 저장, 5분 후 만료
    }

    // 이메일로 인증 번호 조회
    public AuthNumDto findByEmailRedis(String email) {
        String authNum = stringRedisTemplate.opsForValue().get(email);
        if (authNum == null) {
            return null;
        }
        return new AuthNumDto(email, authNum);
    }

    // 인증 번호 삭제 (Redis에서 해당 이메일 키 삭제)
    public void deleteExistCodeRedis(String email) {
        stringRedisTemplate.delete(email);
    }

    // 메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {

        createCode(email); // 인증 코드 생성
        String setFrom = "awsbbangjun@gmail.com"; // email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String title = "[RealTimeTrip] 인증 코드는 " + newAuthNum + "입니다"; //제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
        message.setSubject(title); // 제목 설정
        message.setFrom(setFrom); // 송신자 이메일
        message.setText(setContext(newAuthNum), "utf-8", "html");

        return message;
    }

    // 메일 양식 작성
    public MimeMessage createEmailFormRedis(String email) throws MessagingException, UnsupportedEncodingException {

        String setFrom = "awsbbangjun@gmail.com"; // email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String title = "[RealTimeTrip] 인증 코드는 " + newAuthNum + "입니다"; //제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
        message.setSubject(title); // 제목 설정
        message.setFrom(setFrom); // 송신자 이메일
        message.setText(setContext(newAuthNum), "utf-8", "html");

        return message;
    }

    // 타임리프를 이용한 context 설정
    public String setContext(String authNum) {
        Context context = new Context();
        context.setVariable("code", authNum); // 생성한 인증 번호가 th:text="${code}와 매핑
        return templateEngine.process("mail", context); // mail.html
    }

    // 한 유저가 2번 이상 연속으로 인증 코드를 보낼 경우에 대한 예외 처리를 위해 기존의 코드 삭제
    @Transactional
    public void deleteExistCode(String email){
        authNumRepository.deleteByEmail(email);
    }

    public void deleteExpiredAuthNum(){
        List<AuthNum> authNumList = authNumRepository.findAll();

        for(AuthNum authNum : authNumList){
            // 현재 시간 - 인증 번호 발급 시간 계산
            Duration duration = Duration.between(authNum.getCreated_at(), LocalDateTime.now());

            if(duration.toMinutes() >= 5){
                authNumRepository.delete(authNum);
            }
        }
    }

    @Transactional
    public Boolean checkAuthNum(String email, String code) {
        AuthNum authNum = authNumRepository.findByEmail(email);

        String authNumStr = String.valueOf(authNum.getAuthNum());

        if (code.equals(authNumStr)) {
            authNumRepository.deleteByEmail(email);
            return true;
//            LocalDateTime now = LocalDateTime.now();
//            Duration duration = Duration.between(authNumDto.getCreated_at(), now);
//
//            if (duration.toMinutes() < 5) { // 5분 이하로 인증 코드를 맞춘 경우
//                authNumRepository.deleteByEmail(email);
//                return "Success";
//            }
//            else{
//                authNumRepository.deleteByEmail(email);
//                return "Error: over 5 minute";
//            }
        }
//        // 인증 코드가 틀린 경우
//        return "Error: not correct auth code";

        return false;
    }

    public AuthNumDto findByEmail(String email){
        return AuthNumDto.toAuthNumDto(authNumRepository.findByEmail(email));
    }
}
