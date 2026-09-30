package com.gsuarez.technicaltest.adapter.out.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsuarez.technicaltest.application.port.out.SearchRepository;
import com.gsuarez.technicaltest.domain.Search;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SearchKafkaConsumerTest {

	private final SearchRepository searchRepository = mock(SearchRepository.class);
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final SearchKafkaConsumer consumer = new SearchKafkaConsumer(searchRepository, objectMapper);

	@Test
	void consumePersistsValidMessage() {
		String message = """
				{
				  "searchId": "abc-123",
				  "hotelId": "1234aBc",
				  "checkIn": "2023-12-29",
				  "checkOut": "2023-12-31",
				  "ages": [30, 29, 1, 3]
				}
				""";

		consumer.consume(message);

		verify(searchRepository).save(any(Search.class));
	}

	@Test
	void consumeThrowsOnInvalidMessage() {
		String invalidMessage = "not valid json";

		assertThrows(SearchConsumeException.class, () -> consumer.consume(invalidMessage));
	}
}
