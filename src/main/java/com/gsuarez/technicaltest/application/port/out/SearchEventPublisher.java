package com.gsuarez.technicaltest.application.port.out;

import com.gsuarez.technicaltest.domain.Search;

public interface SearchEventPublisher {

	void publish(Search search);
}
