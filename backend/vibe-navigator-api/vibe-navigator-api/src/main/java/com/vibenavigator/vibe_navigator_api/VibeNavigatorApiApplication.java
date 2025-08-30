// In VibeNavigatorApiApplication.java
package com.vibenavigator.vibe_navigator_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories; // <-- REMOVE THIS IMPORT
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.vibenavigator.vibe_navigator_api.repository.jpa")
// @EnableElasticsearchRepositories(basePackages = "com.vibenavigator.vibe_navigator_api.repository.elasticsearch") // <-- REMOVE THIS LINE
public class VibeNavigatorApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VibeNavigatorApiApplication.class, args);
	}

}