package com.karthik.movieTweets.producer;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.karthik.movieTweets.config.KafkaConfiguration;
import com.karthik.movieTweets.config.TwitterConfiguration;
import com.karthik.movieTweets.model.Tweet;
import com.karthik.movieTweets.producer.callback.BasicCallback;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

@Component
public class TwitterKafkaProducer {
	private Client client;
	private BlockingQueue<String> queue;
	private Gson gson;
	private Callback callback;

	@Autowired
	private TwitterConfiguration twitterConfiguration;

	@Autowired
	private KafkaConfiguration kafkaConfiguration;

	public void run(String... args) {
		System.out.println("=====================Configurations======================");
		System.out.println(kafkaConfiguration);
		System.out.println(twitterConfiguration);
		System.out.println("===========================================");

	}

	public TwitterKafkaProducer() {
		// Configure auth
		super();
		//initialize();
	}

	public void initialize() {
		Authentication authentication = new OAuth1(twitterConfiguration.getConsumerKey(),
				twitterConfiguration.getConsumerSecret(), twitterConfiguration.getAccessToken(),
				twitterConfiguration.getTokenSecret());

		// track the terms of your choice. here im only tracking #bigdata.
		StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
		endpoint.trackTerms(Collections.singletonList("#master"));

		queue = new LinkedBlockingQueue<>(10000);

		client = new ClientBuilder().hosts(Constants.STREAM_HOST).authentication(authentication).endpoint(endpoint)
				.processor(new StringDelimitedProcessor(queue)).build();
		gson = new Gson();
		callback = new BasicCallback();
	}
	private Producer<Long, String> getProducer() {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfiguration.getServers());
		properties.put(ProducerConfig.ACKS_CONFIG, "1");
		properties.put(ProducerConfig.LINGER_MS_CONFIG, 500);
		properties.put(ProducerConfig.RETRIES_CONFIG, 0);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		return new KafkaProducer<>(properties);
	}

	public void run() {
		client.connect();
		try (Producer<Long, String> producer = getProducer()) {
			while (true) {
				Tweet tweet = gson.fromJson(queue.take(), Tweet.class);
				//System.out.printf("Fetched tweet id %d\n", tweet.getId());
				long key = tweet.getId();
				String msg = tweet.toString();
				ProducerRecord<Long, String> record = new ProducerRecord<>(kafkaConfiguration.getTopic(), key, msg);
				producer.send(record, callback);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			client.stop();
		}
	}
}