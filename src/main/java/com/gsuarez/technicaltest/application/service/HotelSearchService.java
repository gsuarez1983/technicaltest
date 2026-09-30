package com.gsuarez.technicaltest.application.service;

import com.gsuarez.technicaltest.application.port.in.HotelSearchUseCase;
import com.gsuarez.technicaltest.application.port.out.SearchEventPublisher;
import com.gsuarez.technicaltest.application.port.out.SearchRepository;
import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HotelSearchService implements HotelSearchUseCase {

	private final SearchEventPublisher searchEventPublisher;
	private final SearchRepository searchRepository;

	public HotelSearchService(SearchEventPublisher searchEventPublisher, SearchRepository searchRepository) {
		this.searchEventPublisher = searchEventPublisher;
		this.searchRepository = searchRepository;
	}

	@Override
	@Transactional
	public SearchId search(Search search) {
		Search searchWithId = search.withSearchId(SearchId.generate());
		searchEventPublisher.publish(searchWithId);
		return searchWithId.searchId();
	}

	@Override
	@Transactional(readOnly = true)
	public CountResult count(SearchId searchId) {
		Search search = searchRepository.findById(searchId)
				.orElseThrow(() -> new SearchNotFoundException(searchId));
		long count = searchRepository.countEqualSearches(search);
		return new CountResult(searchId, search, count);
	}
}
