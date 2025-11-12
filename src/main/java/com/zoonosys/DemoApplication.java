package com.zoonosys;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("TOKEN_SECRET_KEY", dotenv.get("TOKEN_SECRET_KEY"));
        System.setProperty("TOKEN_ISSUER", dotenv.get("TOKEN_ISSUER"));
        System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("spring.mail.username", dotenv.get("MAIL_USERNAME"));
        System.setProperty("spring.mail.password", dotenv.get("MAIL_PASSWORD"));

        SpringApplication.run(DemoApplication.class, args);
	}
}
