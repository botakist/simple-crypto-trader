package com.demo.simplecryptotrader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SimpleCryptoTraderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleCryptoTraderApplication.class, args);
	}

}
