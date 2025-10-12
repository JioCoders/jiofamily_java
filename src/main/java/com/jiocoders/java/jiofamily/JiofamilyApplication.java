package com.jiocoders.java.jiofamily;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class JiofamilyApplication {
	private static final String TAG = JiofamilyApplication.class.getSimpleName();

	public static void main(String[] args) {
		applicationContext = SpringApplication.run(JiofamilyApplication.class, args);
		logger.info("****************-----App-Started-----****************--> " + TAG);
	}

	private static ConfigurableApplicationContext applicationContext;
	private static final Logger logger = LoggerFactory.getLogger(JiofamilyApplication.class);
}
