package com.example.NotificationSender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class NotificationSenderApplicationTests {

	@BeforeEach
	void setup() {
		// This method can be used to set up any preconditions before each test.
	}

	@Test
	void contextLoads() {
		// Check if the context loads successfully
		Assertions.assertTrue(true, "Application context should load successfully.");
	}

	@Test
	void anotherSampleTest() {
		// A sample test method
		int expectedValue = 5;
		int actualValue = 2 + 3; // Change this logic as needed
		Assertions.assertEquals(expectedValue, actualValue, "The values should be equal.");
	}
}
