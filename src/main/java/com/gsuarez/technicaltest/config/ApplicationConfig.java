package com.gsuarez.technicaltest.config;

import com.gsuarez.technicaltest.application.port.in.HotelSearchUseCase;
import com.gsuarez.technicaltest.application.port.out.SearchEventPublisher;
import com.gsuarez.technicaltest.application.port.out.SearchRepository;
import com.gsuarez.technicaltest.application.port.out.TransactionRunner;
import com.gsuarez.technicaltest.application.service.HotelSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

	@Bean
	HotelSearchUseCase hotelSearchUseCase(SearchEventPublisher searchEventPublisher,
			SearchRepository searchRepository, TransactionRunner transactionRunner) {
		return new HotelSearchService(searchEventPublisher, searchRepository, transactionRunner);
	}
}
