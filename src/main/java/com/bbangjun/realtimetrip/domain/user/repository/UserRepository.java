package com.bbangjun.realtimetrip.domain.user.repository;

import com.bbangjun.realtimetrip.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
