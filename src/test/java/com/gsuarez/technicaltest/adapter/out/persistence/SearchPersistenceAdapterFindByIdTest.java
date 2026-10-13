package com.gsuarez.technicaltest.adapter.out.persistence;

import com.gsuarez.technicaltest.application.port.out.SearchRepository;
import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchPersistenceAdapterFindByIdTest {

	private final SearchJpaRepository jpaRepository = mock(SearchJpaRepository.class);
	private final SearchRepository repository = new SearchPersistenceAdapter(jpaRepository);

	@Test
	void findByIdReturnsSearchWhenExists() {
		SearchEntity entity = new SearchEntity("id-1", "1234aBc",
				LocalDate.now().plusDays(30), LocalDate.now().plusDays(32),
				"1,3,29,30", LocalDateTime.now());
		when(jpaRepository.findBySearchId("id-1")).thenReturn(Optional.of(entity));

		Optional<Search> result = repository.findById(new SearchId("id-1"));

		assertAll("findById result",
				() -> assertTrue(result.isPresent()),
				() -> assertEquals("id-1", result.get().searchId().value()),
				() -> assertEquals("1234aBc", result.get().hotelId()),
				() -> assertEquals(List.of(1, 3, 29, 30), result.get().ages())
		);
	}

	@Test
	void findByIdReturnsEmptyWhenNotExists() {
		when(jpaRepository.findBySearchId("missing")).thenReturn(Optional.empty());

		Optional<Search> result = repository.findById(new SearchId("missing"));

		assertTrue(result.isEmpty());
	}
}
