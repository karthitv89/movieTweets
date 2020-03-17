package com.karthik.movieTweets.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/*@Component
@Configuration*/
//@ConstructorBinding
@Component
@PropertySource("classpath:kafka.properties")
@ConfigurationProperties(value = "kafka")
public class KafkaConfiguration {

	private String servers;

	private String topic;

	private long sleepTimer;

	private long maxPollRecords;

	private String offsetResetEarlier;

	public String getServers() {
		return servers;
	}

	public void setServers(String servers) {
		this.servers = servers;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public long getSleepTimer() {
		return sleepTimer;
	}

	public void setSleepTimer(long sleepTimer) {
		this.sleepTimer = sleepTimer;
	}

	@Override
	public String toString() {
		return "KafkaConfiguration [servers=" + servers + ", topic=" + topic + ", sleepTimer=" + sleepTimer
				+ ", maxPollRecords=" + maxPollRecords + ", offsetResetEarlier=" + offsetResetEarlier + "]";
	}

	public long getMaxPollRecords() {
		return maxPollRecords;
	}

	public void setMaxPollRecords(long maxPollRecords) {
		this.maxPollRecords = maxPollRecords;
	}

	public String getOffsetResetEarlier() {
		return offsetResetEarlier;
	}

	public void setOffsetResetEarlier(String offsetResetEarlier) {
		this.offsetResetEarlier = offsetResetEarlier;
	}

}
