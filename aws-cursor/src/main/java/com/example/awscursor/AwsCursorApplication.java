package com.example.awscursor;

import com.amazonaws.services.s3.AmazonS3;
import com.example.awscursor.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@AllArgsConstructor
@SpringBootApplication
public class AwsCursorApplication implements CommandLineRunner {

	private final UserRepo userRepo;
	private final AmazonS3 s3;

	public static void main(String[] args) {
		SpringApplication.run(AwsCursorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Long id = 1L;
		System.out.println(userRepo.findById(id));

		s3.putObject("cursor-backet-lessons-test", "file-name", new File("aws-cursor/src/main/java/com/example/awscursor/s3/test.txt"));
	}
}
