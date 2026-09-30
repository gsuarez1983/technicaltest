package com.gsuarez.technicaltest.adapter.in.web;

import com.gsuarez.technicaltest.application.port.in.HotelSearchUseCase;
import com.gsuarez.technicaltest.application.service.SearchNotFoundException;
import com.gsuarez.technicaltest.domain.SearchId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HotelControllerExceptionTest {

	private MockMvc mockMvc;
	private HotelSearchUseCase hotelSearchUseCase;

	@BeforeEach
	void setUp() {
		hotelSearchUseCase = mock(HotelSearchUseCase.class);
		mockMvc = MockMvcBuilders.standaloneSetup(new HotelController(hotelSearchUseCase))
				.build();
	}

	@Test
	void countReturns404WhenSearchNotFound() throws Exception {
		when(hotelSearchUseCase.count(any(SearchId.class)))
				.thenThrow(new SearchNotFoundException(new SearchId("missing")));

		mockMvc.perform(get("/count").param("searchId", "missing"))
				.andExpect(status().isNotFound());
	}

	@Test
	void searchReturns400WhenDomainValidationFails() throws Exception {
		String payload = """
				{
				  "hotelId": "1234aBc",
				  "checkIn": "31/12/2023",
				  "checkOut": "29/12/2023",
				  "ages": [30, 29, 1, 3]
				}
				""";

		mockMvc.perform(post("/search")
						.contentType("application/json")
						.content(payload))
				.andExpect(status().isBadRequest());
	}

	@Test
	void searchReturns400WhenAgesEmpty() throws Exception {
		String payload = """
				{
				  "hotelId": "1234aBc",
				  "checkIn": "29/12/2023",
				  "checkOut": "31/12/2023",
				  "ages": []
				}
				""";

		mockMvc.perform(post("/search")
						.contentType("application/json")
						.content(payload))
				.andExpect(status().isBadRequest());
	}
}
