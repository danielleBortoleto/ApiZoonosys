package com.zoonosys.auth;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("TOKEN_SECRET_KEY", dotenv.get("TOKEN_SECRET_KEY"));
        System.setProperty("TOKEN_ISSUER", dotenv.get("TOKEN_ISSUER"));

        SpringApplication.run(DemoApplication.class, args);
	}

}
