package com.karthik.movieTweets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karthik.movieTweets.consumer.SimpleKafkaConsumer;
import com.karthik.movieTweets.producer.TwitterKafkaProducer;

@RestController
public class HomeController {

	@Autowired
	private TwitterKafkaProducer twitterKafkaProducer;
	
	@Autowired
	private SimpleKafkaConsumer simpleKafkaConsumer;

	@RequestMapping("/tweetEater")
	public String readTweets() {
		twitterKafkaProducer.initialize();
		twitterKafkaProducer.run();

		return "Hello World!";
	}
	
	@RequestMapping("/cunsumeTweets")
	public String consumeTweets() {
		simpleKafkaConsumer.createConsumer();
		simpleKafkaConsumer.runConsumer();
		return "Hello World!";
	}
}
