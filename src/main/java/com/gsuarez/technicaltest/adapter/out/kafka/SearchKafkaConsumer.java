package com.gsuarez.technicaltest.adapter.out.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsuarez.technicaltest.application.port.out.SearchRepository;
import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@Component
public class SearchKafkaConsumer {

	private final SearchRepository searchRepository;
	private final ObjectMapper objectMapper;
	private volatile CountDownLatch testLatch;

	public SearchKafkaConsumer(SearchRepository searchRepository, ObjectMapper objectMapper) {
		this.searchRepository = searchRepository;
		this.objectMapper = objectMapper;
	}

	@KafkaListener(
			topics = SearchKafkaProducer.TOPIC,
			groupId = "${spring.kafka.consumer.group-id}",
			containerFactory = "kafkaListenerContainerFactory"
	)
	public void consume(String message) {
		Search search = parseMessage(message);
		
		CompletableFuture.runAsync(() -> searchRepository.save(search))
				.exceptionally(ex -> {
					throw new SearchConsumeException("Failed to persist search: " + message, ex);
				})
				.thenRun(() -> { if (testLatch != null) testLatch.countDown(); });
	}

	void setTestLatch(CountDownLatch latch) {
		this.testLatch = latch;
	}

	private Search parseMessage(String message) {
		try {
			SearchKafkaProducer.SearchMessage searchMessage = objectMapper.readValue(message,
					SearchKafkaProducer.SearchMessage.class);
			return new Search(
					new SearchId(searchMessage.searchId()),
					searchMessage.hotelId(),
					LocalDate.parse(searchMessage.checkIn()),
					LocalDate.parse(searchMessage.checkOut()),
					searchMessage.ages());
		} catch (Exception e) {
			throw new SearchConsumeException("Failed to consume search message: " + message, e);
		}
	}
}
