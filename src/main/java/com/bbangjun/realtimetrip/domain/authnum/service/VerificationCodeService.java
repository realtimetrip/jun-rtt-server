package com.bbangjun.realtimetrip.domain.authnum.service;

import com.bbangjun.realtimetrip.domain.authnum.dto.VerificationCodeRequestDto;
import com.bbangjun.realtimetrip.domain.authnum.entity.VerificationCode;
import com.bbangjun.realtimetrip.domain.authnum.repository.VerificationCodeRepository;
import com.bbangjun.realtimetrip.global.response.BaseResponse;
import com.bbangjun.realtimetrip.global.response.ResponseCode;
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

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class VerificationCodeService {

    // 실제 메일 전송을 위한 send() 메서드 사용
    private final JavaMailSender emailSender;
    private String newVerificationCode;

    // 타임리프를 이용한 context 설정
    private final SpringTemplateEngine templateEngine;
    // DB 저장 방식
    private final VerificationCodeRepository verificationCodeRepository;
    // redis 방식
    private final StringRedisTemplate stringRedisTemplate;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // [db 저장 방식 인증 번호] step1: controller에서 호출
    public void sendEmailDB(String toEmail) throws MessagingException {

        // 메일 전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(toEmail);
        // 실제 메일 전송
        emailSender.send(emailForm);
    }

    // [db 저장 방식 인증 번호] step2: 메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException {

        createCode(email); // 인증 코드 생성
        String setFrom = "awsbbangjun@gmail.com"; // email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String title = "[RealTimeTrip] 인증 코드는 " + newVerificationCode + "입니다"; //제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
        message.setSubject(title); // 제목 설정
        message.setFrom(setFrom); // 송신자 이메일
        message.setText(setContext(newVerificationCode), "utf-8", "html");

        return message;
    }

    // [db 저장 방식 인증 번호] step3: 랜덤 인증 코드 생성
    public void createCode(String email) {
        Random random = new Random();
        newVerificationCode = String.valueOf(random.nextInt(9000)+1000); // 범위: 1000 ~ 9999

        VerificationCode verificationCode = VerificationCode.builder()
                .verificationCode(newVerificationCode)
                .created_at(LocalDateTime.now())
                .email(email).build();

        verificationCodeRepository.save(verificationCode);

        // 생성된 인증 번호가 5분 뒤 자동으로 삭제되도록 스케줄링
        scheduler.schedule(() -> deleteCode(newVerificationCode, email), 5, TimeUnit.MINUTES);

    }

    // [db 저장 방식 인증 번호] step4: 타임리프를 이용한 context 설정
    public String setContext(String authNum) {
        Context context = new Context();
        context.setVariable("code", authNum); // 생성한 인증 번호가 th:text="${code}와 매핑
        return templateEngine.process("mail", context); // mail.html
    }

    private void deleteCode(String code, String email) {
        VerificationCode verificationCode = verificationCodeRepository.findByVerificationCodeAndEmail(code, email);
        if (verificationCode != null) {
            verificationCodeRepository.delete(verificationCode);
        }
    }

    // 한 유저가 2번 이상 연속으로 인증 코드를 보낼 경우에 대한 예외 처리를 위해 기존의 코드 삭제
    @Transactional
    public void deleteExistCode(String email){
        verificationCodeRepository.deleteByEmail(email);
    }


    // [redis 저장 방식 인증 번호] step1: 타임리프를 이용한 context 설정
    public void sendEmailRedis(String toEmail) throws MessagingException {

        // 메일 전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailFormRedis(toEmail);
        createCodeRedis(toEmail);
        // 실제 메일 전송
        emailSender.send(emailForm);
    }

    // [redis 저장 방식 인증 번호] step2: 메일 양식 작성
    public MimeMessage createEmailFormRedis(String email) throws MessagingException {

        String setFrom = "awsbbangjun@gmail.com"; // email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String title = "[RealTimeTrip] 인증 코드는 " + newVerificationCode + "입니다"; //제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
        message.setSubject(title); // 제목 설정
        message.setFrom(setFrom); // 송신자 이메일
        message.setText(setContext(newVerificationCode), "utf-8", "html");

        return message;
    }

    // [redis 저장 방식 인증 번호] step3: 인증 번호 생성 및 Redis 저장
    private void createCodeRedis(String email) {
        Random random = new Random();
        newVerificationCode = String.valueOf(random.nextInt(9000) + 1000); // 1000 ~ 9999 범위의 인증 번호 생성

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(email, newVerificationCode, 5, TimeUnit.MINUTES); // Redis에 이메일을 키로, 인증 번호를 값으로 저장, 5분 후 만료
    }

    // [redis 저장 방식 인증 번호] step4: 이메일로 인증 번호 조회
    public VerificationCodeRequestDto findByEmailRedis(String email) {
        String verificationCode = stringRedisTemplate.opsForValue().get(email);
        if (verificationCode == null) {
            return null;
        }
        return new VerificationCodeRequestDto(email, verificationCode);
    }

    // [redis 저장 방식 인증 번호] 인증 번호 삭제 (Redis에서 해당 이메일 키 삭제)
    public void deleteExistCodeRedis(String email) {
        stringRedisTemplate.delete(email);
    }

    // [DB 이메일 인증 번호 검증]
    @Transactional
    public BaseResponse<Object> checkVerificationCode(String email, String requestVerificationCode) {
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);

        if (requestVerificationCode.equals(verificationCode.getVerificationCode())) {
            deleteCode(requestVerificationCode, email);
            return new BaseResponse<>();
        }
        else
            return new BaseResponse<>(ResponseCode.INCORRECT_VERIFICATIONCODE);
    }

    public VerificationCodeRequestDto findByEmailDB(String email){
        return VerificationCodeRequestDto.toVerificationCodeDto(verificationCodeRepository.findByEmail(email));
    }
}
