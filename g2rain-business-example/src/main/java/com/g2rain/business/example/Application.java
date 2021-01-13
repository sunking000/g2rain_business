package com.g2rain.business.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.lettuce.core.RedisURI;

@EnableDiscoveryClient
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
		// SpringApplication.run(Application.class, args);
		RedisURI redisURI = RedisURI.Builder.redis("localhost", 6379).withPassword("password").withPort(6379)
				.withDatabase(1).build();
		System.out.println(redisURI.toURI().toString());
    }
}