package com.example.hospitalreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HospitalReviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalReviewApplication.class, args);
	}

}
