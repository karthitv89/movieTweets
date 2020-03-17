package com.karthik.movieTweets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@ConfigurationPropertiesScan("com.karthik.movieTweets.config")

public class MovieTweetsApplication {
	@Value("${spring.application.name}")
	private String name;

	public static void main(String[] args) {
		SpringApplication.run(MovieTweetsApplication.class, args);
	}
	
	/*
	 * @RequestMapping(value = "/") public String getName() { return "Karthik"; }
	 */

}
