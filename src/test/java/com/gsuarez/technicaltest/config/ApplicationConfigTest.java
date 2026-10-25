package com.gsuarez.technicaltest.config;

import com.gsuarez.technicaltest.application.port.in.HotelSearchUseCase;
import com.gsuarez.technicaltest.application.port.out.SearchEventPublisher;
import com.gsuarez.technicaltest.application.port.out.SearchRepository;
import com.gsuarez.technicaltest.application.port.out.TransactionRunner;
import com.gsuarez.technicaltest.application.service.HotelSearchService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;

class ApplicationConfigTest {

	@Test
	void hotelSearchUseCaseBeanIsCreated() {
		SearchEventPublisher publisher = mock(SearchEventPublisher.class);
		SearchRepository repository = mock(SearchRepository.class);
		TransactionRunner transactionRunner = mock(TransactionRunner.class);

		HotelSearchUseCase useCase =
				new ApplicationConfig().hotelSearchUseCase(publisher, repository, transactionRunner);

		assertInstanceOf(HotelSearchService.class, useCase);
	}
}
