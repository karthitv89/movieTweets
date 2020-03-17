package com.karthik.movieTweets.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.karthik.movieTweets.config.KafkaConfiguration;

@Component
public class SimpleKafkaConsumer {

	@Autowired
	private KafkaConfiguration kafkaConfiguration;

	public Consumer<Long, String> createConsumer() {
		Properties consumerProps = new Properties();
		consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfiguration.getServers());
		consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroup1"/* kafkaConfiguration.consumerGroup1() */);
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConfiguration.getMaxPollRecords());
		consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConfiguration.getOffsetResetEarlier());

		Consumer<Long, String> consumer = new KafkaConsumer<Long, String>(consumerProps);
		consumer.subscribe(Collections.singletonList(kafkaConfiguration.getTopic()));
		return consumer;
	}

	public void runConsumer() {
		final int giveUp = 100;
		int noRecordsCount = 0;

		final Consumer<Long, String> consumer = createConsumer();
		while (true) {
			final ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofSeconds(5));
			if (consumerRecords.isEmpty()) {
				noRecordsCount++;
				if (noRecordsCount > giveUp)
					break;
				else
					continue;
			}

			// process tweets
			consumerRecords.forEach(record -> {
				System.out.printf("Consumer Record:(%d, %s, %d, %d)\n", record.key(), record.value(),
						record.partition(), record.offset());
			});

			consumer.commitAsync();
		}

		consumer.close();
		System.out.println("DONE");
	}

}
