package com.bbangjun.realtimetrip.authnum.service;

import com.bbangjun.realtimetrip.authnum.dto.AuthNumDto;
import com.bbangjun.realtimetrip.authnum.entity.AuthNum;
import com.bbangjun.realtimetrip.authnum.repository.AuthNumRepository;
import com.bbangjun.realtimetrip.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthNumService {

    // 실제 메일 전송을 위한 send() 메서드 사용
    private final JavaMailSender emailSender;
    // 이메일 인증 코드에 대한 CRUD를 수행
    private final AuthNumRepository authNumRepository;
    // email로 해당 user의 id 찾기
    private final UserRepository userRepository;
    // 생성된 인증 번호
    private String newAuthNum;
    // 타임리프를 이용한 context 설정
    private final SpringTemplateEngine templateEngine;

    // 실제 메일 전송 - controller에서 호출
    public String sendEmail(String toEmail) throws MessagingException, UnsupportedEncodingException {

        // 메일 전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(toEmail);
        // 실제 메일 전송
        emailSender.send(emailForm);

        return newAuthNum; // 인증 코드 반환
    }

    // 메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {

        createCode(email); // 인증 코드 생성
        String setFrom = "awsbbangjun@gmail.com"; // email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String toEmail = email; // 받는 사람
        String title = "[RealTimeTrip] 인증 코드는 " + newAuthNum + "입니다"; //제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
        message.setSubject(title); // 제목 설정
        message.setFrom(setFrom); // 송신자 이메일
        message.setText(setContext(newAuthNum), "utf-8", "html");

        return message;
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

    // 타임리프를 이용한 context 설정
    public String setContext(String authNum) {
        Context context = new Context();
        context.setVariable("code", authNum); // 생성한 인증 번호가 th:text="${code}와 매핑
        return templateEngine.process("mail", context); // mail.html
    }

    // 한 유저가 2번 이상 연속으로 인증 코드를 보낼 경우에 대한 예외 처리를 위해 기존의 코드 삭제
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

    public String verifyCode(String email, String code) {
        AuthNumDto authNumDto = authNumRepository.findByEmail(email);

        String authNumStr = String.valueOf(authNumDto.getAuthNum());

        if (code.equals(authNumStr)) {
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(authNumDto.getCreated_at(), now);

            if (duration.toMinutes() < 5) { // 5분 이하로 인증 코드를 맞춘 경우
                authNumRepository.deleteByEmail(email);
                return "Success";
            }
            else{
                authNumRepository.deleteByEmail(email);
                return "Error: over 5 minute";
            }
        }
        // 인증 코드가 틀린 경우
        return "Error: not correct auth code";
    }
}
