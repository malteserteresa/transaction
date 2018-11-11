package com.n26;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO SMOTHER IN TESTS. Refactor controller - HTTP best practices. DDD
 * architecture . Async. Add UI
 * 
 * @author tcake
 *
 */

@SpringBootApplication
public class Application {

	public static void main(String... args) {
		SpringApplication.run(Application.class, args);
	}

}
