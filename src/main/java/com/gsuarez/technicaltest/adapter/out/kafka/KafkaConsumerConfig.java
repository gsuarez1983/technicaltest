package com.gsuarez.technicaltest.adapter.out.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
			ConsumerFactory<String, String> consumerFactory) {

		ConcurrentKafkaListenerContainerFactory<String, String> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		factory.setConcurrency(6);
		factory.getContainerProperties().setAckMode(
				org.springframework.kafka.listener.ContainerProperties.AckMode.RECORD);
		return factory;
	}
}