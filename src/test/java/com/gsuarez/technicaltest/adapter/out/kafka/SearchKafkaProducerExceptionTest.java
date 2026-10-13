package com.gsuarez.technicaltest.adapter.out.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SearchKafkaProducerExceptionTest {

	@SuppressWarnings("unchecked")
	private final KafkaTemplate<String, String> kafkaTemplate = mock(KafkaTemplate.class);
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final SearchKafkaProducer producer = new SearchKafkaProducer(kafkaTemplate, objectMapper);

	@Test
	void publishThrowsWhenKafkaFails() {
		Search search = new Search(new SearchId("abc-123"), "1234aBc",
				LocalDate.now().plusDays(30), LocalDate.now().plusDays(32),
				List.of(30, 29, 1, 3));
		when(kafkaTemplate.send(eq(SearchKafkaProducer.TOPIC), anyString(), anyString()))
				.thenThrow(new RuntimeException("Kafka unavailable"));

		assertThrows(SearchPublishException.class, () -> producer.publish(search));
	}
}
