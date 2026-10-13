package com.gsuarez.technicaltest.application.port.out;

import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SearchRepository {

	void save(Search search);

	Optional<Search> findById(SearchId searchId);

	Optional<Search> findByHotelIdAndDatesAndAges(String hotelId, LocalDate checkIn, LocalDate checkOut, List<Integer> ages);

	long countEqualSearches(Search search);
}
