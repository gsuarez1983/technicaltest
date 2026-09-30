package com.gsuarez.technicaltest.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchIdTest {

	@Test
	void generateCreatesNonBlankId() {
		SearchId searchId = SearchId.generate();

		assertNotNull(searchId);
		assertFalse(searchId.value().isBlank());
	}

	@Test
	void generateCreatesUniqueIds() {
		SearchId id1 = SearchId.generate();
		SearchId id2 = SearchId.generate();

		assertNotEquals(id1.value(), id2.value());
	}

	@Test
	void throwsWhenValueBlank() {
		assertThrows(IllegalArgumentException.class, () -> new SearchId(""));
	}

	@Test
	void throwsWhenValueNull() {
		assertThrows(IllegalArgumentException.class, () -> new SearchId(null));
	}

	@Test
	void toStringReturnsValue() {
		SearchId searchId = new SearchId("abc-123");

		assertEquals("abc-123", searchId.toString());
	}
}
