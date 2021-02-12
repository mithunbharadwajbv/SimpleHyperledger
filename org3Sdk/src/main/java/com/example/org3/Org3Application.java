package com.example.org3;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Org3Application implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(Org3Application.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/greet").allowedOrigins("http://localhost:9000");
				registry.addMapping("/**");
			}
		};
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

	}
}
