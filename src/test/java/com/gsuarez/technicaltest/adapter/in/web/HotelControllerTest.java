package com.gsuarez.technicaltest.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsuarez.technicaltest.application.port.in.HotelSearchUseCase;
import com.gsuarez.technicaltest.domain.Search;
import com.gsuarez.technicaltest.domain.SearchId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HotelControllerTest {

	private MockMvc mockMvc;
	private HotelSearchUseCase hotelSearchUseCase;

	@BeforeEach
	void setUp() {
		hotelSearchUseCase = mock(HotelSearchUseCase.class);
		mockMvc = MockMvcBuilders.standaloneSetup(new HotelController(hotelSearchUseCase))
				.setControllerAdvice(new ValidationExceptionHandler())
				.build();
	}

	@Test
	void searchReturns202WithSearchId() throws Exception {
		SearchId searchId = new SearchId("abc-123");
		when(hotelSearchUseCase.search(any(Search.class))).thenReturn(searchId);

		String payload = """
				{
				  "hotelId": "1234aBc",
				  "checkIn": "29/12/2023",
				  "checkOut": "31/12/2023",
				  "ages": [30, 29, 1, 3]
				}
				""";

		mockMvc.perform(post("/search")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.searchId").value("abc-123"));
	}

	@Test
	void searchReturns400WithErrorsWhenHotelIdBlank() throws Exception {
		String payload = """
				{
				  "hotelId": "",
				  "checkIn": "29/12/2023",
				  "checkOut": "31/12/2023",
				  "ages": [30, 29, 1, 3]
				}
				""";

		mockMvc.perform(post("/search")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.hotelId").value("hotelId must not be blank"));
	}

	@Test
	void searchReturns400WithErrorsWhenHotelIdNotAlphanumeric() throws Exception {
		String payload = """
				{
				  "hotelId": "123-abc",
				  "checkIn": "29/12/2023",
				  "checkOut": "31/12/2023",
				  "ages": [30, 29, 1, 3]
				}
				""";

		mockMvc.perform(post("/search")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.hotelId").value("hotelId must be alphanumeric"));
	}

	@Test
	void searchReturns400WithErrorsWhenCheckInBlank() throws Exception {
		String payload = """
				{
				  "hotelId": "1234aBc",
				  "checkIn": "",
				  "checkOut": "31/12/2023",
				  "ages": [30, 29, 1, 3]
				}
				""";

		mockMvc.perform(post("/search")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.checkIn").value("checkIn must not be blank"));
	}

	@Test
	void searchReturns400WithErrorsWhenCheckOutBlank() throws Exception {
		String payload = """
				{
				  "hotelId": "1234aBc",
				  "checkIn": "29/12/2023",
				  "checkOut": "",
				  "ages": [30, 29, 1, 3]
				}
				""";

		mockMvc.perform(post("/search")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.checkOut").value("checkOut must not be blank"));
	}

	@Test
	void searchReturns400WithErrorsWhenDateFormatInvalid() throws Exception {
		String payload = """
				{
				  "hotelId": "1234aBc",
				  "checkIn": "21/122/2026",
				  "checkOut": "31/12/2023",
				  "ages": [30, 29, 1, 3]
				}
				""";

		mockMvc.perform(post("/search")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.checkIn").value("checkIn must match format dd/MM/yyyy"));
	}

	@Test
	void searchReturns400WithErrorsWhenAgesEmpty() throws Exception {
		String payload = """
				{
				  "hotelId": "1234aBc",
				  "checkIn": "29/12/2023",
				  "checkOut": "31/12/2023",
				  "ages": []
				}
				""";

		mockMvc.perform(post("/search")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.ages").value("ages must not be empty"));
	}

	@Test
	void searchReturns400WithErrorsWhenAgeNegative() throws Exception {
		String payload = """
				{
				  "hotelId": "1234aBc",
				  "checkIn": "29/12/2023",
				  "checkOut": "31/12/2023",
				  "ages": [30, -1, 3]
				}
				""";

		mockMvc.perform(post("/search")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.ages").value("each age must be positive"));
	}

	@Test
	void searchReturns400WhenDatesInvalid() throws Exception {
		String payload = """
				{
				  "hotelId": "1234aBc",
				  "checkIn": "31/12/2023",
				  "checkOut": "29/12/2023",
				  "ages": [30, 29, 1, 3]
				}
				""";

		mockMvc.perform(post("/search")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(status().isBadRequest());
	}

	@Test
	void countReturnsSearchAndCount() throws Exception {
		SearchId searchId = new SearchId("abc-123");
		Search search = new Search(searchId, "1234aBc",
				LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
				List.of(30, 29, 1, 3));
		when(hotelSearchUseCase.count(any(SearchId.class)))
				.thenReturn(new HotelSearchUseCase.CountResult(searchId, search, 100));

		mockMvc.perform(get("/count").param("searchId", "abc-123"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.searchId").value("abc-123"))
				.andExpect(jsonPath("$.search.hotelId").value("1234aBc"))
				.andExpect(jsonPath("$.search.checkIn").value("29/12/2023"))
				.andExpect(jsonPath("$.search.checkOut").value("31/12/2023"))
				.andExpect(jsonPath("$.search.ages[0]").value(1))
				.andExpect(jsonPath("$.search.ages[1]").value(3))
				.andExpect(jsonPath("$.search.ages[2]").value(29))
				.andExpect(jsonPath("$.search.ages[3]").value(30))
				.andExpect(jsonPath("$.count").value(100));
	}
}
