package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//Indicates a configuration class that declares one or more @Bean methods and also triggers auto-configuration and component scanning.
//This is a convenienceannotation that is equivalent to declaring @Configuration, @EnableAutoConfiguration and @ComponentScan.
@EnableScheduling
//Indicates a configuration class that declares one or more @Bean methods and also triggers auto-configuration and component scanning. 
//This is a convenienceannotation that is equivalent to declaring @Configuration, @EnableAutoConfiguration and @ComponentScan.
public class CalendarApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarApplication.class, args); // Class that can be used to bootstrap and launch a
																// Spring application from a Java mainmethod.
		// By default class will perform the following steps to bootstrap
		// your application:
	}

}
