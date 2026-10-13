package com.gsuarez.technicaltest.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

	private static final LocalDate CHECK_IN = LocalDate.now().plusDays(30);
	private static final LocalDate CHECK_OUT = LocalDate.now().plusDays(32);

	@Test
	void createsValidSearch() {
		Search search = new Search(new SearchId("id-1"), "1234aBc", CHECK_IN, CHECK_OUT, List.of(30, 29, 1, 3));

		assertAll("Search",
				() -> assertEquals("id-1", search.searchId().value()),
				() -> assertEquals("1234aBc", search.hotelId()),
				() -> assertEquals(CHECK_IN, search.checkIn()),
				() -> assertEquals(CHECK_OUT, search.checkOut()),
				() -> assertEquals(List.of(30, 29, 1, 3), search.ages())
		);
	}

	@Test
	void throwsWhenHotelIdBlank() {
		SearchId searchId = new SearchId("id-1");
		assertThrows(IllegalArgumentException.class,
				() -> new Search(searchId, "", CHECK_IN, CHECK_OUT, List.of(30)));
	}

	@Test
	void throwsWhenCheckInNull() {
		SearchId searchId = new SearchId("id-1");
		assertThrows(IllegalArgumentException.class,
				() -> new Search(searchId, "1234aBc", null, CHECK_OUT, List.of(30)));
	}

	@Test
	void throwsWhenCheckOutNull() {
		SearchId searchId = new SearchId("id-1");
		assertThrows(IllegalArgumentException.class,
				() -> new Search(searchId, "1234aBc", CHECK_IN, null, List.of(30)));
	}

	@Test
	void throwsWhenCheckOutBeforeCheckIn() {
		SearchId searchId = new SearchId("id-1");
		assertThrows(IllegalArgumentException.class,
				() -> new Search(searchId, "1234aBc", CHECK_OUT, CHECK_IN, List.of(30)));
	}

	@Test
	void throwsWhenAgesEmpty() {
		SearchId searchId = new SearchId("id-1");
		assertThrows(IllegalArgumentException.class,
				() -> new Search(searchId, "1234aBc", CHECK_IN, CHECK_OUT, List.of()));
	}

	@Test
	void throwsWhenCheckInInPast() {
		SearchId searchId = new SearchId("id-1");
		assertThrows(IllegalArgumentException.class,
				() -> new Search(searchId, "1234aBc", LocalDate.now().minusDays(1), CHECK_OUT, List.of(30)));
	}

	@Test
	void withSearchIdReturnsNewSearchWithId() {
		Search original = new Search(null, "1234aBc", CHECK_IN, CHECK_OUT, List.of(30, 29));
		Search withId = original.withSearchId(new SearchId("new-id"));

		assertAll("Search with new id",
				() -> assertEquals("new-id", withId.searchId().value()),
				() -> assertEquals(original.hotelId(), withId.hotelId()),
				() -> assertEquals(original.checkIn(), withId.checkIn()),
				() -> assertEquals(original.checkOut(), withId.checkOut()),
				() -> assertEquals(original.ages(), withId.ages())
		);
	}
}
