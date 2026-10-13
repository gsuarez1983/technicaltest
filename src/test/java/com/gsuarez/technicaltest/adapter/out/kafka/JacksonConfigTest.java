package com.gsuarez.technicaltest.adapter.out.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JacksonConfigTest {

	@Test
	void objectMapperBeanIsCreated() {
		JacksonConfig config = new JacksonConfig();
		ObjectMapper objectMapper = config.objectMapper();

		assertNotNull(objectMapper);
	}
}
