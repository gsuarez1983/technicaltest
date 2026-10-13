package com.gsuarez.technicaltest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.junit.jupiter.api.Assertions.*;

class TechnicaltestApplicationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withUserConfiguration(TechnicaltestApplication.class);

	@Test
	void contextLoads() {
		contextRunner.run(context -> assertNotNull(context));
	}

}
