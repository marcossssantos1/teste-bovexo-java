package com.agro.feedcost.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(FeedCostNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(FeedCostNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now()));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		String message = "Valor inválido para feedType: " + ex.getValue()
				+ ". Valores aceitos: MILHO, SOJA, FARELO_SOJA, SORGO, TRIGO, "
				+ "SUPLEMENTO_MINERAL, NUCLEO_PROTEICO, SAL_BRANCO, UREIA, SILAGEM_MILHO";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, LocalDateTime.now()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor", LocalDateTime.now()));
	}

}