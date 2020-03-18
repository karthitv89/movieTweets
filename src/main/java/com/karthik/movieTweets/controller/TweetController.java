package com.karthik.movieTweets.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karthik.movieTweets.spark.driver.SparkDriver;

@RestController("tweet")
public class TweetController {
	
	@Autowired
	private SparkDriver sparkDriver;
	
	@RequestMapping("analizeBadWords/{hashTag}") 
	public Map<String, Integer> analizeBadWords(@PathVariable String hashTag) throws InterruptedException {
		sparkDriver.run();
		return null;
		
	}
	

}
