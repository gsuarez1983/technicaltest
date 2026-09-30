package com.gsuarez.technicaltest.adapter.out.persistence;

import com.gsuarez.technicaltest.application.port.out.SearchRepository;
import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchPersistenceAdapterTest {

	private final SearchJpaRepository jpaRepository = mock(SearchJpaRepository.class);
	private final SearchRepository repository = new SearchPersistenceAdapter(jpaRepository);

	@Test
	void saveSerializesAgesSorted() {
		Search search = new Search(new SearchId("id-1"), "1234aBc",
				LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
				List.of(30, 29, 3, 1));

		repository.save(search);

		verify(jpaRepository).save(argThat(entity ->
				"1,3,29,30".equals(entity.getAges())));
	}

	@Test
	void countEqualSearchesUsesSortedAges() {
		Search search = new Search(new SearchId("id-1"), "1234aBc",
				LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
				List.of(30, 29, 1, 3));

		repository.countEqualSearches(search);

		verify(jpaRepository).countEqualSearches(
				eq("1234aBc"),
				eq(LocalDate.of(2023, 12, 29)),
				eq(LocalDate.of(2023, 12, 31)),
				eq("1,3,29,30"));
	}
}
