package com.gsuarez.technicaltest.adapter.in.web;

import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class SearchMapper {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private SearchMapper() {
	}

	public static Search toDomain(SearchRequest request) {
		LocalDate checkIn = parseDate(request.checkIn(), "checkIn");
		LocalDate checkOut = parseDate(request.checkOut(), "checkOut");
		return new Search(null, request.hotelId(), checkIn, checkOut, request.ages());
	}

	public static Search toDomain(String searchId, String hotelId, String checkIn, String checkOut, java.util.List<Integer> ages) {
		return new Search(new SearchId(searchId), hotelId, parseDate(checkIn, "checkIn"),
				parseDate(checkOut, "checkOut"), ages);
	}

	public static CountResponse.SearchPayload toPayload(Search search) {
		return new CountResponse.SearchPayload(
				search.hotelId(),
				search.checkIn().format(DATE_FORMATTER),
				search.checkOut().format(DATE_FORMATTER),
				search.ages().stream().sorted().toList());
	}

	private static LocalDate parseDate(String value, String field) {
		try {
			return LocalDate.parse(value, DATE_FORMATTER);
		} catch (DateTimeParseException e) {
			throw new InvalidDateException(field + " must match format dd/MM/yyyy: " + value);
		}
	}
}
