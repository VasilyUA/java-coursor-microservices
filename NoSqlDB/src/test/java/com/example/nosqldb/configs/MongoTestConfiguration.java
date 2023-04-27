package com.example.nosqldb.configs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class MongoTestConfiguration extends AbstractMongoClientConfiguration {

	private static final MongoDBContainer MONGO_DB_CONTAINER;

	static {
		DockerImageName mongoImage = DockerImageName.parse("mongo:4.4.6");
		MONGO_DB_CONTAINER = new MongoDBContainer(mongoImage);
		MONGO_DB_CONTAINER.start();
	}

	@Bean
	@Primary
	@Override
	public @NotNull MongoClient mongoClient() {
		return MongoClients.create(MONGO_DB_CONTAINER.getReplicaSetUrl());
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), getDatabaseName());
	}

	@Override
	protected @NotNull String getDatabaseName() {
		return "test";
	}
}
