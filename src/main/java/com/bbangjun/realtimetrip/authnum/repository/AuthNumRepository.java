package com.bbangjun.realtimetrip.authnum.repository;

import com.bbangjun.realtimetrip.authnum.entity.AuthNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthNumRepository extends JpaRepository<AuthNum, Long> {
}
