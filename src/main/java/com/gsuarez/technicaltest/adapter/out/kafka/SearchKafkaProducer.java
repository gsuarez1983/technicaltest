package com.gsuarez.technicaltest.adapter.out.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsuarez.technicaltest.application.port.out.SearchEventPublisher;
import com.gsuarez.technicaltest.domain.Search;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SearchKafkaProducer implements SearchEventPublisher {

	public static final String TOPIC = "hotel_availability_searches";

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public SearchKafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
	}

	@Override
	public void publish(Search search) {
		try {
			String payload = objectMapper.writeValueAsString(new SearchMessage(
					search.searchId().value(),
					search.hotelId(),
					search.checkIn().toString(),
					search.checkOut().toString(),
					search.ages()));
			kafkaTemplate.send(TOPIC, search.searchId().value(), payload);
		} catch (Exception e) {
			throw new SearchPublishException("Failed to publish search to Kafka", e);
		}
	}

	public record SearchMessage(
			String searchId,
			String hotelId,
			String checkIn,
			String checkOut,
			java.util.List<Integer> ages) {
	}
}
