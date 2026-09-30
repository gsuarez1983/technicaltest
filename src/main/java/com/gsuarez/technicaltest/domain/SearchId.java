package com.gsuarez.technicaltest.domain;

import java.util.UUID;

public record SearchId(String value) {

	public SearchId {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("searchId must not be blank");
		}
	}

	public static SearchId generate() {
		return new SearchId(UUID.randomUUID().toString());
	}

	@Override
	public String toString() {
		return value;
	}
}
