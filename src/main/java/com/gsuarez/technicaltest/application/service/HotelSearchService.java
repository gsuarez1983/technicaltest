package com.gsuarez.technicaltest.application.service;

import com.gsuarez.technicaltest.application.port.in.HotelSearchUseCase;
import com.gsuarez.technicaltest.application.port.out.SearchEventPublisher;
import com.gsuarez.technicaltest.application.port.out.SearchRepository;
import com.gsuarez.technicaltest.application.port.out.TransactionRunner;
import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;

public class HotelSearchService implements HotelSearchUseCase {

	private final SearchEventPublisher searchEventPublisher;
	private final SearchRepository searchRepository;
	private final TransactionRunner transactionRunner;

	public HotelSearchService(SearchEventPublisher searchEventPublisher, SearchRepository searchRepository,
			TransactionRunner transactionRunner) {
		this.searchEventPublisher = searchEventPublisher;
		this.searchRepository = searchRepository;
		this.transactionRunner = transactionRunner;
	}

	@Override
	public SearchId search(Search search) {
		return transactionRunner.write(() ->
			searchRepository.findByHotelIdAndDatesAndAges(
					search.hotelId(), search.checkIn(), search.checkOut(), search.ages())
				.map(Search::searchId)
				.orElseGet(() -> {
					Search searchWithId = search.withSearchId(SearchId.generate());
					searchRepository.save(searchWithId);
					searchEventPublisher.publish(searchWithId);
					return searchWithId.searchId();
				}));
	}

	@Override
	public CountResult count(SearchId searchId) {
		return transactionRunner.readOnly(() -> {
			Search search = searchRepository.findById(searchId)
					.orElseThrow(() -> new SearchNotFoundException(searchId));
			long count = searchRepository.countEqualSearches(search);
			return new CountResult(searchId, search, count);
		});
	}
}
