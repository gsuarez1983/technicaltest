package com.gsuarez.technicaltest.application.port.out;

import java.util.function.Supplier;

public interface TransactionRunner {

	<T> T write(Supplier<T> action);

	<T> T readOnly(Supplier<T> action);
}
