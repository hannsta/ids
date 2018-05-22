package com.cop.ids;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.mongodb.MongoClient;
@Configuration
@EnableMongoRepositories
public class AppConfig extends AbstractMongoConfiguration {

   
   @Override
   protected String getDatabaseName() {
     return "ids";
   }
   @Override
   protected String getMappingBasePackage() {
     return "com.cop.ids.repositories";
   }

	@Override
	@Bean
	public MongoClient mongoClient() {
        MongoClient client = new MongoClient("localhost");
        return client;
	}
    
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(10);
		pool.setMaxPoolSize(50);
		pool.setWaitForTasksToCompleteOnShutdown(true);
		return pool;
	}
} 
