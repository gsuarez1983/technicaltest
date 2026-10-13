package com.gsuarez.technicaltest.adapter.in.web;

import com.gsuarez.technicaltest.application.port.in.HotelSearchUseCase;
import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
	@Operation(summary = "Creates a new hotel search", description = "Receives hotel search criteria, validates domain rules, persists the search and publishes an event. Returns the generated search ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "Search created successfully"),
			@ApiResponse(responseCode = "400", description = "Validation failure: invalid dates, checkOut before checkIn, empty ages, or blank hotelId")
	})
	public ResponseEntity<SearchIdResponse> search(@Valid @RequestBody SearchRequest request) {
		Search search = SearchMapper.toDomain(request);
		SearchId searchId = hotelSearchUseCase.search(search);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SearchIdResponse(searchId.value()));
	}

	@GetMapping("/count")
	@Operation(summary = "Counts matching hotel searches", description = "Finds a previously stored search by its ID and returns how many times the same search criteria have been submitted.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Search found with its match count"),
			@ApiResponse(responseCode = "404", description = "No search exists with the given searchId")
	})
	public CountResponse count(@Parameter(name = "searchId", description = "The unique identifier of a previously created search", required = true) @RequestParam("searchId") String searchId) {
		HotelSearchUseCase.CountResult result = hotelSearchUseCase.count(new SearchId(searchId));
		return new CountResponse(result.searchId().value(), SearchMapper.toPayload(result.search()), result.count());
	}

}
