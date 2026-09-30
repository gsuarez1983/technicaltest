package com.gsuarez.technicaltest.application.port.in;

import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;

public interface HotelSearchUseCase {

	SearchId search(Search search);

	CountResult count(SearchId searchId);

	record CountResult(SearchId searchId, Search search, long count) {
	}
}
