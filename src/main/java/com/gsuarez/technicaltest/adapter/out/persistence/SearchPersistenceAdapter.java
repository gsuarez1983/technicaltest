package com.gsuarez.technicaltest.adapter.out.persistence;

import com.gsuarez.technicaltest.application.port.out.SearchRepository;
import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SearchPersistenceAdapter implements SearchRepository {

	private final SearchJpaRepository jpaRepository;

	public SearchPersistenceAdapter(SearchJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public void save(Search search) {
		jpaRepository.save(toEntity(search));
	}

	@Override
	public Optional<Search> findById(SearchId searchId) {
		return jpaRepository.findBySearchId(searchId.value()).map(this::toDomain);
	}

	@Override
	public long countEqualSearches(Search search) {
		return jpaRepository.countEqualSearches(
				search.hotelId(),
				search.checkIn(),
				search.checkOut(),
				serializeAges(search.ages()));
	}

	private SearchEntity toEntity(Search search) {
		return new SearchEntity(
				search.searchId().value(),
				search.hotelId(),
				search.checkIn(),
				search.checkOut(),
				serializeAges(search.ages()),
				LocalDateTime.now());
	}

	private Search toDomain(SearchEntity entity) {
		return new Search(
				new SearchId(entity.getSearchId()),
				entity.getHotelId(),
				entity.getCheckIn(),
				entity.getCheckOut(),
				deserializeAges(entity.getAges()));
	}

	private static String serializeAges(java.util.List<Integer> ages) {
		return ages.stream()
				.sorted()
				.map(String::valueOf)
				.collect(Collectors.joining(","));
	}

	private static java.util.List<Integer> deserializeAges(String ages) {
		return Arrays.stream(ages.split(","))
				.map(Integer::parseInt)
				.toList();
	}
}
