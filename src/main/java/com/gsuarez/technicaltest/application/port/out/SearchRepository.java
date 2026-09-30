package com.gsuarez.technicaltest.application.port.out;

import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;

import java.util.Optional;

public interface SearchRepository {

	void save(Search search);

	Optional<Search> findById(SearchId searchId);

	long countEqualSearches(Search search);
}
