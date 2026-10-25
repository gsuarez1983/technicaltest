package com.gsuarez.technicaltest.adapter.out.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.SimpleTransactionStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionAdapterTest {

	private final PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
	private final TransactionAdapter adapter = new TransactionAdapter(transactionManager);

	@BeforeEach
	void setUp() {
		when(transactionManager.getTransaction(any())).thenReturn(new SimpleTransactionStatus());
	}

	@Test
	void writeExecutesActionAndCommits() {
		assertEquals(42, adapter.write(() -> 42));

		verify(transactionManager).commit(any());
		verify(transactionManager, never()).rollback(any());
	}

	@Test
	void writeUsesReadWriteTransactionDefinition() {
		adapter.write(() -> "done");

		assertFalse(capturedDefinition().isReadOnly());
	}

	@Test
	void readOnlyUsesReadOnlyTransactionDefinition() {
		adapter.readOnly(() -> "done");

		assertTrue(capturedDefinition().isReadOnly());
	}

	@Test
	void writeRollsBackWhenActionFails() {
		assertThrows(IllegalStateException.class,
				() -> adapter.write(() -> { throw new IllegalStateException("boom"); }));

		verify(transactionManager).rollback(any());
		verify(transactionManager, never()).commit(any());
	}

	private TransactionDefinition capturedDefinition() {
		ArgumentCaptor<TransactionDefinition> captor = ArgumentCaptor.forClass(TransactionDefinition.class);
		verify(transactionManager).getTransaction(captor.capture());
		return captor.getValue();
	}
}
