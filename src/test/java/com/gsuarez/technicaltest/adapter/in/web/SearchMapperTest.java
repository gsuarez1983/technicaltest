package com.gsuarez.technicaltest.adapter.in.web;

import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchMapperTest {

	@Test
	void toDomainParsesValidRequest() {
		LocalDate checkIn = LocalDate.now().plusDays(30);
		LocalDate checkOut = LocalDate.now().plusDays(32);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		SearchRequest request = new SearchRequest("1234aBc", checkIn.format(formatter), checkOut.format(formatter), List.of(30, 29, 1, 3));

		Search search = SearchMapper.toDomain(request);

		assertAll("toDomain",
				() -> assertEquals("1234aBc", search.hotelId()),
				() -> assertEquals(checkIn, search.checkIn()),
				() -> assertEquals(checkOut, search.checkOut()),
				() -> assertEquals(List.of(30, 29, 1, 3), search.ages())
		);
	}

	@Test
	void toDomainThrowsOnInvalidDateFormat() {
		SearchRequest request = new SearchRequest("1234aBc", "2023-12-29", "31/12/2023", List.of(30));

		assertThrows(InvalidDateException.class, () -> SearchMapper.toDomain(request));
	}

	@Test
	void toDomainWithSearchIdParsesCorrectly() {
		LocalDate checkIn = LocalDate.now().plusDays(30);
		LocalDate checkOut = LocalDate.now().plusDays(32);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Search search = SearchMapper.toDomain("id-1", "1234aBc", checkIn.format(formatter), checkOut.format(formatter), List.of(30, 29));

		assertEquals("id-1", search.searchId().value());
		assertEquals("1234aBc", search.hotelId());
	}

	@Test
	void toPayloadFormatsSearchWithSortedAges() {
		LocalDate checkIn = LocalDate.now().plusDays(30);
		LocalDate checkOut = LocalDate.now().plusDays(32);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Search search = new Search(new SearchId("id-1"), "1234aBc",
				checkIn, checkOut,
				List.of(30, 29, 1, 3));

		CountResponse.SearchPayload payload = SearchMapper.toPayload(search);

		assertAll("toPayload",
				() -> assertEquals("1234aBc", payload.hotelId()),
				() -> assertEquals(checkIn.format(formatter), payload.checkIn()),
				() -> assertEquals(checkOut.format(formatter), payload.checkOut()),
				() -> assertEquals(List.of(1, 3, 29, 30), payload.ages())
		);
	}
}
