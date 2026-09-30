package com.gsuarez.technicaltest.application.service;

import com.gsuarez.technicaltest.application.port.in.HotelSearchUseCase;
import com.gsuarez.technicaltest.application.port.out.SearchEventPublisher;
import com.gsuarez.technicaltest.application.port.out.SearchRepository;
import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HotelSearchServiceTest {

	private final SearchEventPublisher publisher = mock(SearchEventPublisher.class);
	private final SearchRepository repository = mock(SearchRepository.class);
	private final HotelSearchUseCase service = new HotelSearchService(publisher, repository);

	@Test
	void searchGeneratesIdAndPublishes() {
		Search search = new Search(null, "1234aBc",
				LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
				List.of(30, 29, 1, 3));

		SearchId searchId = service.search(search);

		assertNotNull(searchId);
		assertFalse(searchId.value().isBlank());
		verify(publisher).publish(argThat(s -> s.searchId().equals(searchId)));
	}

	@Test
	void countReturnsResultFromRepository() {
		SearchId searchId = new SearchId("abc-123");
		Search search = new Search(searchId, "1234aBc",
				LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
				List.of(30, 29, 1, 3));
		when(repository.findById(searchId)).thenReturn(Optional.of(search));
		when(repository.countEqualSearches(search)).thenReturn(100L);

		HotelSearchUseCase.CountResult result = service.count(searchId);

		assertEquals(100L, result.count());
		assertEquals(search, result.search());
		assertEquals(searchId, result.searchId());
	}

	@Test
	void countThrowsWhenSearchNotFound() {
		SearchId searchId = new SearchId("missing");
		when(repository.findById(searchId)).thenReturn(Optional.empty());

		assertThrows(SearchNotFoundException.class, () -> service.count(searchId));
	}
}
