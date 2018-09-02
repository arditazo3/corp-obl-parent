package com.tx.co;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class CorporateObligationsApplication {

	private static final Logger logger = LogManager.getLogger(CorporateObligationsApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(CorporateObligationsApplication.class, args);

		logger.info("Application Started!");
	}
}
