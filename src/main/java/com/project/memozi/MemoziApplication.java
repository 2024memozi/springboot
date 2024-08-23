package com.project.memozi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MemoziApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemoziApplication.class, args);
	}

}
