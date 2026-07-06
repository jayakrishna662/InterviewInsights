package com.interviewinsights.interviewinsights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class InterviewInsightsApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterviewInsightsApplication.class, args);
	}

}
