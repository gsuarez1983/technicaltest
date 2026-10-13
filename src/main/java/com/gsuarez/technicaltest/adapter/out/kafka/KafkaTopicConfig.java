package com.gsuarez.technicaltest.adapter.out.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public Object hotelAvailabilitySearches() {
		return TopicBuilder.name(SearchKafkaProducer.TOPIC)
				.partitions(6)
				.replicas(1)
				.build();
	}
}