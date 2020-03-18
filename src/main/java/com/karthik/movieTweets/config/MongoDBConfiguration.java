package com.karthik.movieTweets.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@Service
public class MongoDBConfiguration {

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public MongoClient mongoClient(@Value("${spring.mongodb.uri}") String connectionString) {

		// TODO> Ticket: Handling Timeouts - configure the expected
		// WriteConcern `wtimeout` and `connectTimeoutMS` conn

		ConnectionString connString = new ConnectionString(connectionString);

		WriteConcern wc = WriteConcern.MAJORITY.withWTimeout(2500, TimeUnit.MILLISECONDS);
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).writeConcern(wc)
				.build();

		MongoClient mongoClient = MongoClients.create(settings);

		/*
		 * String connString2 =
		 * "mongodb+srv://mflixAppUser:mflixAppPwd@mflix-anj2d.mongodb.net/test?retryWrites=true&w=majority";
		 * MongoClientSettings settingsTest = MongoClientSettings.builder()
		 * .applyConnectionString(new ConnectionString(connString2)).build();
		 * MongoClient mongoClientTest = MongoClients.create(settingsTest);
		 * 
		 * // TODO do a read on the cluster to ensure you are connected
		 * mongoClientTest.getDatabase("sample_mflix").getCollection("comments").find();
		 * 
		 * SslSettings sslSettings = settingsTest.getSslSettings();
		 * System.out.println("sslSettings.isEnabled()::" + sslSettings.isEnabled());
		 * ReadPreference readPreference = settingsTest.getReadPreference();
		 * System.out.println("readPreference.toString()::" +
		 * readPreference.toString()); ReadConcern readConcern =
		 * settingsTest.getReadConcern();
		 * System.out.println("readConcern.asDocument().toString()::" +
		 * readConcern.asDocument().toString()); WriteConcern writeConcern =
		 * settingsTest.getWriteConcern();
		 * System.out.println("writeConcern.asDocument().toString()::" +
		 * writeConcern.asDocument().toString());
		 * System.out.println("sslSettings.isInvalidHostNameAllowed()::" +
		 * sslSettings.isInvalidHostNameAllowed());
		 */
		return mongoClient;

	}
}
