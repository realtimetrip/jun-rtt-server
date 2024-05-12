package com.bbangjun.realtimetrip;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class RealtimetripApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealtimetripApplication.class, args);
	}
}
