package dev.stockflow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Testcontainers
@SpringBootTest
class StockflowApplicationTests {

	@Container
	@ServiceConnection 
	static PostgreSQLContainer postgres =
		new PostgreSQLContainer("postgres:17-alpine");

	@Test
	void contextLoads() {
	}

}
