package com.gsuarez.technicaltest.adapter.in.web;

import com.gsuarez.technicaltest.application.service.SearchNotFoundException;
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
			String message = error.getDefaultMessage();
			errors.compute(field, (k, existing) -> existing == null || isBlankMessage(message) ? message : existing);
		});
		return Map.of("errors", errors);
	}

	private static boolean isBlankMessage(String message) {
		return message != null && message.contains("must not be blank");
	}

	@ExceptionHandler(InvalidDateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleInvalidDate(InvalidDateException ex) {
		return Map.of("errors", Map.of("date", ex.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleIllegalArgument(IllegalArgumentException ex) {
		return Map.of("errors", Map.of("validation", ex.getMessage()));
	}

	@ExceptionHandler(SearchNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, Object> handleSearchNotFound(SearchNotFoundException ex) {
		return Map.of("errors", Map.of("search", ex.getMessage()));
	}

	private static String normalizeField(String field) {
		Matcher matcher = INDEXED_FIELD.matcher(field);
		return matcher.matches() ? matcher.group(1) : field;
	}
}
