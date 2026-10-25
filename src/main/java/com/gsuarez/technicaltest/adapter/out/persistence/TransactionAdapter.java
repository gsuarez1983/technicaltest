package com.gsuarez.technicaltest.adapter.out.persistence;

import com.gsuarez.technicaltest.application.port.out.TransactionRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

@Component
public class TransactionAdapter implements TransactionRunner {

	private final TransactionTemplate writeTemplate;
	private final TransactionTemplate readOnlyTemplate;

	public TransactionAdapter(PlatformTransactionManager transactionManager) {
		this.writeTemplate = new TransactionTemplate(transactionManager);
		this.readOnlyTemplate = new TransactionTemplate(transactionManager);
		this.readOnlyTemplate.setReadOnly(true);
	}

	@Override
	public <T> T write(Supplier<T> action) {
		return writeTemplate.execute(status -> action.get());
	}

	@Override
	public <T> T readOnly(Supplier<T> action) {
		return readOnlyTemplate.execute(status -> action.get());
	}
}
