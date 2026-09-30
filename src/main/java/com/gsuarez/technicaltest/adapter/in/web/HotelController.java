package com.gsuarez.technicaltest.adapter.in.web;

import com.gsuarez.technicaltest.application.port.in.HotelSearchUseCase;
import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HotelController {

	private final HotelSearchUseCase hotelSearchUseCase;

	public HotelController(HotelSearchUseCase hotelSearchUseCase) {
		this.hotelSearchUseCase = hotelSearchUseCase;
	}

	@PostMapping("/search")
	public ResponseEntity<SearchIdResponse> search(@Valid @RequestBody SearchRequest request) {
		Search search = SearchMapper.toDomain(request);
		SearchId searchId = hotelSearchUseCase.search(search);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SearchIdResponse(searchId.value()));
	}

	@GetMapping("/count")
	public CountResponse count(@RequestParam("searchId") String searchId) {
		HotelSearchUseCase.CountResult result = hotelSearchUseCase.count(new SearchId(searchId));
		return new CountResponse(result.searchId().value(), SearchMapper.toPayload(result.search()), result.count());
	}

}
