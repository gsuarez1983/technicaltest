package com.gsuarez.technicaltest.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class ValidationExceptionHandler {

	private static final Pattern INDEXED_FIELD = Pattern.compile("^(\\w+)\\[\\d+]$");

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			String field = normalizeField(error.getField());
			errors.putIfAbsent(field, error.getDefaultMessage());
		});
		return Map.of("errors", errors);
	}

	@ExceptionHandler(InvalidDateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleInvalidDate(InvalidDateException ex) {
		return Map.of("errors", Map.of("date", ex.getMessage()));
	}

	private static String normalizeField(String field) {
		Matcher matcher = INDEXED_FIELD.matcher(field);
		return matcher.matches() ? matcher.group(1) : field;
	}
}
