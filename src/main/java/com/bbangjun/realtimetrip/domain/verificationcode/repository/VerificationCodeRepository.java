package com.bbangjun.realtimetrip.domain.verificationcode.repository;

import com.bbangjun.realtimetrip.domain.verificationcode.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    void deleteByEmail(String email);

    VerificationCode findByEmail(String email);

    VerificationCode findByVerificationCodeAndEmail(String verificationCode, String email);
}
