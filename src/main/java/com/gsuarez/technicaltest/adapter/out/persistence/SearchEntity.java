package com.gsuarez.technicaltest.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "hotel_availability_search",
		indexes = @Index(name = "idx_hotel_search_criteria",
				columnList = "hotel_id, check_in, check_out, ages"))
public class SearchEntity {

	@Id
	@Column(name = "search_id", length = 64)
	private String searchId;

	@Column(name = "hotel_id", nullable = false, length = 64)
	private String hotelId;

	@Column(name = "check_in", nullable = false)
	private LocalDate checkIn;

	@Column(name = "check_out", nullable = false)
	private LocalDate checkOut;

	@Column(name = "ages", nullable = false, length = 512)
	private String ages;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	protected SearchEntity() {
	}

	public SearchEntity(String searchId, String hotelId, LocalDate checkIn, LocalDate checkOut, String ages,
			LocalDateTime createdAt) {
		this.searchId = searchId;
		this.hotelId = hotelId;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.ages = ages;
		this.createdAt = createdAt;
	}

	public String getSearchId() {
		return searchId;
	}

	public String getHotelId() {
		return hotelId;
	}

	public LocalDate getCheckIn() {
		return checkIn;
	}

	public LocalDate getCheckOut() {
		return checkOut;
	}

	public String getAges() {
		return ages;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
