package com.bbangjun.realtimetrip.user.repository;

import com.bbangjun.realtimetrip.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
