package com.example.nosqldb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@SpringBootApplication
@EnableFeignClients("com.example.nosqldb.restclients")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class NoSqlDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoSqlDbApplication.class, args);
	}

}
