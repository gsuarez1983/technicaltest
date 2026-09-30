package com.gsuarez.technicaltest.adapter.in.web;

import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchMapperTest {

	@Test
	void toDomainParsesValidRequest() {
		SearchRequest request = new SearchRequest("1234aBc", "29/12/2023", "31/12/2023", List.of(30, 29, 1, 3));

		Search search = SearchMapper.toDomain(request);

		assertEquals("1234aBc", search.hotelId());
		assertEquals(LocalDate.of(2023, 12, 29), search.checkIn());
		assertEquals(LocalDate.of(2023, 12, 31), search.checkOut());
		assertEquals(List.of(30, 29, 1, 3), search.ages());
	}

	@Test
	void toDomainThrowsOnInvalidDateFormat() {
		SearchRequest request = new SearchRequest("1234aBc", "2023-12-29", "31/12/2023", List.of(30));

		assertThrows(InvalidDateException.class, () -> SearchMapper.toDomain(request));
	}

	@Test
	void toDomainWithSearchIdParsesCorrectly() {
		Search search = SearchMapper.toDomain("id-1", "1234aBc", "29/12/2023", "31/12/2023", List.of(30, 29));

		assertEquals("id-1", search.searchId().value());
		assertEquals("1234aBc", search.hotelId());
	}

	@Test
	void toPayloadFormatsSearchWithSortedAges() {
		Search search = new Search(new SearchId("id-1"), "1234aBc",
				LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
				List.of(30, 29, 1, 3));

		CountResponse.SearchPayload payload = SearchMapper.toPayload(search);

		assertEquals("1234aBc", payload.hotelId());
		assertEquals("29/12/2023", payload.checkIn());
		assertEquals("31/12/2023", payload.checkOut());
		assertEquals(List.of(1, 3, 29, 30), payload.ages());
	}
}
