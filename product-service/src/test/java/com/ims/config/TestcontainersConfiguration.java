package com.ims.config;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestcontainersConfiguration {

	static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
			.withDatabaseName("test_db")
			.withUsername("test_user")
			.withPassword("test_pass");

	@BeforeAll
	static void startContainer() {
		mysql.start();
	}

	@AfterAll
	static void stopContainer() {
		mysql.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
		registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
	}

	@Test
	void testContainerRunning() {
		assertThat(mysql.isRunning()).isTrue();
	}
}
