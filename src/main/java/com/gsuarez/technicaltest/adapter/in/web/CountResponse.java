package com.gsuarez.technicaltest.adapter.in.web;

import java.util.List;

public record CountResponse(
		String searchId,
		SearchPayload search,
		long count) {

	public record SearchPayload(
			String hotelId,
			String checkIn,
			String checkOut,
			List<Integer> ages) {
	}
}
