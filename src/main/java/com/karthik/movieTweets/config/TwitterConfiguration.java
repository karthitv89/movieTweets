package com.karthik.movieTweets.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/*
@Component
@Configuration*/
@Component
@PropertySource("classpath:twitter.properties")
//@ConstructorBinding
@ConfigurationProperties(value = "twitter")
public class TwitterConfiguration {

	// @Value("${consumer.key}")
	private String consumerKey;

	// @Value("${consumer.secret}")
	private String consumerSecret;

	// @Value("${access.token}")
	private String accessToken;

	// @Value("${access.secret}")
	private String tokenSecret;
	// private String HASHTAG = "#bigdata";

	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	@Override
	public String toString() {
		return "TwitterConfiguration [consumerKey=" + consumerKey + ", consumerSecret=" + consumerSecret
				+ ", accessToken=" + accessToken + ", tokenSecret=" + tokenSecret + "]";
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

}
