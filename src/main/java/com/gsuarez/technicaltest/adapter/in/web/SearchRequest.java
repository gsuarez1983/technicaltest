package com.gsuarez.technicaltest.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record SearchRequest(
		@NotBlank(message = "hotelId must not be blank")
		@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "hotelId must be alphanumeric")
		String hotelId,

		@NotBlank(message = "checkIn must not be blank")
		@DateFormat(message = "checkIn must match format dd/MM/yyyy")
		String checkIn,

		@NotBlank(message = "checkOut must not be blank")
		@DateFormat(message = "checkOut must match format dd/MM/yyyy")
		String checkOut,

		@NotEmpty(message = "ages must not be empty")
		List<@Positive(message = "each age must be positive") Integer> ages) {
}
