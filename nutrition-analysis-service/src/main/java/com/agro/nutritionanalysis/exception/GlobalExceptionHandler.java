package com.agro.nutritionanalysis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NutritionAnalysisNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(NutritionAnalysisNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now()));
	}

	@ExceptionHandler(RestClientException.class)
	public ResponseEntity<ErrorResponse> handleRestClient(RestClientException ex) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(),
						"Erro ao consultar feed-cost-service: " + ex.getMessage(), LocalDateTime.now()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor", LocalDateTime.now()));
	}

}