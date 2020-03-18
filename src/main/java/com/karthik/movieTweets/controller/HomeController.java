package com.karthik.movieTweets.controller;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karthik.movieTweets.consumer.SimpleKafkaConsumer;
import com.karthik.movieTweets.producer.TwitterKafkaProducer;
import com.karthik.movieTweets.spark.driver.SparkDriver;

@RestController
public class HomeController {

	@Autowired
	private TwitterKafkaProducer twitterKafkaProducer;

	@Autowired
	private SimpleKafkaConsumer simpleKafkaConsumer;

	@Autowired
	private SparkDriver sparkDriver;

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

	@RequestMapping("/tweet/{hashtag}")
	public Integer countTweets(@PathVariable String hashtag) throws InterruptedException {
		sparkDriver.run();
		return null;
	}

	@RequestMapping("/newTweet/{hashTag}")
	public String insertTweet(@PathVariable String hashTag, @RequestParam("msg") String msg) throws InterruptedException {
		Document doc = sparkDriver.insertOne(hashTag, msg);
		sparkDriver.run();
		return doc.toJson();
	}

}
