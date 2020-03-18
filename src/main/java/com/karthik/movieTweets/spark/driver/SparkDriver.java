package com.karthik.movieTweets.spark.driver;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.HasOffsetRanges;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.spark.streaming.kafka010.OffsetRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karthik.movieTweets.consumer.SimpleKafkaConsumer;
import com.karthik.movieTweets.dao.MovieDao;

import scala.Tuple2;

@Service("sparkDriver")
public class SparkDriver {

	@Autowired
	private MovieDao movieDao;

	public Document insertOne(String hashTag, String message) {
		return movieDao.addUser(hashTag, message);

	}

	public void run() throws InterruptedException {
		Map<String, Object> kafkaParams = new HashMap<>();
		kafkaParams.put("bootstrap.servers", "localhost:9092");
		kafkaParams.put("key.deserializer", StringDeserializer.class);
		kafkaParams.put("value.deserializer", StringDeserializer.class);
		kafkaParams.put("group.id", "group1");
		kafkaParams.put("auto.offset.reset", "latest");
		kafkaParams.put("enable.auto.commit", false);
		Collection<String> topics = Arrays.asList("tweets");

		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("tweetSpark");
		sparkConf.setMaster("local");
		/*
		 * SparkSession spark =
		 * SparkSession.builder().config(sparkConf).appName("tweetSpark").master(
		 * "local") .getOrCreate();
		 */

		JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));

		JavaInputDStream<ConsumerRecord<String, String>> tweetStream = KafkaUtils.createDirectStream(streamingContext,
				LocationStrategies.PreferConsistent(),
				ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams));

		tweetStream.foreachRDD(rdd -> {
            System.out.println("--- New RDD with " + rdd.partitions().size()
                    + " partitions and " + rdd.count() + " records");
            rdd.foreach(r -> {
            	String value = r.toString();
            	movieDao.addUser(r.key(), value);
            	System.out.println(r.key() + " :::: " + value);
            });
        });

		streamingContext.start();
		streamingContext.awaitTermination();
 

			// begin your transaction

			// update results
			// update offsets where the end of existing offsets matches the beginning of
			// this batch of offsets
			// assert that offsets were updated correctly

			// end your transaction
		
}

}
