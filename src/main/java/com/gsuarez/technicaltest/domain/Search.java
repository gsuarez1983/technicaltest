package com.gsuarez.technicaltest.domain;

import java.time.LocalDate;
import java.util.List;

public record Search(
		SearchId searchId,
		String hotelId,
		LocalDate checkIn,
		LocalDate checkOut,
		List<Integer> ages) {

	public Search {
		if (hotelId == null || hotelId.isBlank()) {
			throw new IllegalArgumentException("hotelId must not be blank");
		}
		if (checkIn == null || checkOut == null) {
			throw new IllegalArgumentException("checkIn and checkOut must not be null");
		}
		if (!checkOut.isAfter(checkIn)) {
			throw new IllegalArgumentException("checkOut must be after checkIn");
		}
		if (ages == null || ages.isEmpty()) {
			throw new IllegalArgumentException("ages must not be empty");
		}
		ages = List.copyOf(ages);
	}

	public Search withSearchId(SearchId newSearchId) {
		return new Search(newSearchId, hotelId, checkIn, checkOut, ages);
	}
}
