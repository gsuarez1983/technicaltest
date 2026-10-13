package com.gsuarez.technicaltest.adapter.out.persistence;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SearchEntityTest {

	@Test
	void createsEntityWithAllFields() {
		LocalDateTime now = LocalDateTime.now();
		SearchEntity entity = new SearchEntity("id-1", "1234aBc",
				LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
				"1,3,29,30", now);

		assertAll("SearchEntity",
				() -> assertEquals("id-1", entity.getSearchId()),
				() -> assertEquals("1234aBc", entity.getHotelId()),
				() -> assertEquals(LocalDate.of(2023, 12, 29), entity.getCheckIn()),
				() -> assertEquals(LocalDate.of(2023, 12, 31), entity.getCheckOut()),
				() -> assertEquals("1,3,29,30", entity.getAges()),
				() -> assertEquals(now, entity.getCreatedAt())
		);
	}

	@Test
	void defaultConstructorCreatesEmptyEntity() {
		SearchEntity entity = new SearchEntity();

		assertNull(entity.getSearchId());
		assertNull(entity.getHotelId());
	}
}
