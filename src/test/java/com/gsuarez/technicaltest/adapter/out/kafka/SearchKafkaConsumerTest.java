package com.gsuarez.technicaltest.adapter.out.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsuarez.technicaltest.application.port.out.SearchRepository;
import com.gsuarez.technicaltest.domain.Search;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SearchKafkaConsumerTest {

	private final SearchRepository searchRepository = mock(SearchRepository.class);
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final SearchKafkaConsumer consumer = new SearchKafkaConsumer(searchRepository, objectMapper);

	@Test
	void consumePersistsValidMessage() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		consumer.setTestLatch(latch);

		String message = """
				{
				  "searchId": "abc-123",
				  "hotelId": "1234aBc",
				  "checkIn": "%s",
				  "checkOut": "%s",
				  "ages": [30, 29, 1, 3]
				}
				""".formatted(LocalDate.now().plusDays(30), LocalDate.now().plusDays(32));

		consumer.consume(message);

		assertTrue(latch.await(5, TimeUnit.SECONDS));
		verify(searchRepository).save(any(Search.class));
	}

	@Test
	void consumeThrowsOnInvalidMessage() {
		String invalidMessage = "not valid json";

		assertThrows(SearchConsumeException.class, () -> consumer.consume(invalidMessage));
	}
}
