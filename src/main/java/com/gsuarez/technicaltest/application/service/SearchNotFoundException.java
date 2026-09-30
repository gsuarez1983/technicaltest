package com.gsuarez.technicaltest.application.service;

import com.gsuarez.technicaltest.domain.SearchId;

public class SearchNotFoundException extends RuntimeException {

	public SearchNotFoundException(SearchId searchId) {
		super("Search not found: " + searchId.value());
	}
}
