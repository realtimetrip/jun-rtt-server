package com.bbangjun.realtimetrip.domain.verificationcode.service;

import com.bbangjun.realtimetrip.domain.verificationcode.dto.SendVerificationCodeResponseDto;
import com.bbangjun.realtimetrip.domain.verificationcode.entity.VerificationCode;
import com.bbangjun.realtimetrip.domain.verificationcode.repository.VerificationCodeRepository;
import com.bbangjun.realtimetrip.global.response.BaseResponse;
import com.bbangjun.realtimetrip.global.response.ResponseCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
@Service
public class VerificationCodeService {
    private String newVerificationCode;

    // 실제 메일 전송을 위한 send() 메서드 사용
    private final JavaMailSender emailSender;
    // 타임리프를 이용한 context 설정
    private final SpringTemplateEngine templateEngine;
    // DB 저장 방식
    private final VerificationCodeRepository verificationCodeRepository;
    // redis 방식
    private final StringRedisTemplate stringRedisTemplate;
    // Spring Data Redis에서 문자열 값을 처리하기 위해 사용하는 객체. 이 객체를 통해 Redis에 값을 저장하고 가져올 수 있음.
    private final ValueOperations<String, String> valueOperations;

    private final ScheduledExecutorService scheduler;

    @Autowired
    public VerificationCodeService(JavaMailSender emailSender, SpringTemplateEngine templateEngine,
                                   VerificationCodeRepository verificationCodeRepository, StringRedisTemplate stringRedisTemplate) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.verificationCodeRepository = verificationCodeRepository;
        this.stringRedisTemplate = stringRedisTemplate;
        this.valueOperations = stringRedisTemplate.opsForValue();
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    // [db 저장 방식 인증 번호] step1: controller에서 호출
    public void sendEmailDB(String toEmail) throws MessagingException {

        // 인증 코드 생성
        createCode(toEmail);
        // 메일 양식 작성
        MimeMessage emailForm = createEmailForm(toEmail);
        // 실제 메일 전송
        emailSender.send(emailForm);
    }

    // [db 저장 방식 인증 번호 & redis 저장 방식 인증 번호] step2: 메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException {
        
        // email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String setFrom = "awsbbangjun@gmail.com";
        // 제목
        String title = "[RealTimeTrip] 인증 코드는 " + newVerificationCode + "입니다";

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(email); // 수신자 이메일
        helper.setSubject(title); // 제목 설정
        helper.setFrom(setFrom); // 송신자 이메일
        helper.setText(setContext(newVerificationCode), true); // HTML 내용 설정

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
    public String setContext(String verificationCode) {
        Context context = new Context();
        context.setVariable("code", verificationCode); // 생성한 인증 번호가 th:text="${code}와 매핑
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

        createCodeRedis(toEmail);
        // 메일 전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(toEmail);

        // 실제 메일 전송
        emailSender.send(emailForm);
    }

    // step2는 db 저장 방식 인증 번호와 동일한 메서드

    // [redis 저장 방식 인증 번호] step3: 인증 번호 생성 및 Redis 저장
    private void createCodeRedis(String email) {
        Random random = new Random();
        newVerificationCode = String.valueOf(random.nextInt(9000) + 1000); // 1000 ~ 9999 범위의 인증 번호 생성

        // Redis에 이메일을 키로, 인증 번호를 값으로 저장, 5분 후 만료
        valueOperations.set(email, newVerificationCode, 5, TimeUnit.MINUTES);
    }

    // [redis 저장 방식 인증 번호] step4: 이메일을 key로 인증번호를 찾아서 반환
    public SendVerificationCodeResponseDto findByEmailRedis(String email) {
        String verificationCode = stringRedisTemplate.opsForValue().get(email);
        if (verificationCode == null)
            return null;

        return new SendVerificationCodeResponseDto(email, verificationCode);
    }

    // [redis 저장 방식 인증 번호] 인증 번호 삭제 (Redis에서 해당 이메일 키 삭제)
    public void deleteExistCodeRedis(String email) {
        stringRedisTemplate.delete(email);
    }

    // [DB 이메일 인증 번호 검증]
    @Transactional
    public BaseResponse<Object> checkVerificationCodeDB(String email, String requestVerificationCode) {
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);

        if (requestVerificationCode.equals(verificationCode.getVerificationCode())) {
            deleteCode(requestVerificationCode, email);
            return new BaseResponse<>();
        }
        else
            return new BaseResponse<>(ResponseCode.INCORRECT_VERIFICATIONCODE);
    }

    // [redis 이메일 인증 번호 검증]
    public BaseResponse<Object> checkVerificationCodeRedis(String email, String requestVerificationCode) {
        
        String storedVerificationCode = valueOperations.get(email);
        if (storedVerificationCode == null) {
            return new BaseResponse<>(ResponseCode.NOT_FOUND, "값이 존재하지 않습니다.");
        }

        if (storedVerificationCode.equals(requestVerificationCode)) {
            stringRedisTemplate.delete(email);
            return new BaseResponse<>();
        } else {
            return new BaseResponse<>(ResponseCode.INCORRECT_VERIFICATIONCODE);
        }
    }

    public SendVerificationCodeResponseDto findByEmailDB(String email){
        return SendVerificationCodeResponseDto.toVerificationCodeDto(verificationCodeRepository.findByEmail(email));
    }


}
