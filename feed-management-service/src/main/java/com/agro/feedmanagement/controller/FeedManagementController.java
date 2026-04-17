package com.agro.feedmanagement.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agro.feedmanagement.dto.FeedRequest;
import com.agro.feedmanagement.dto.FeedResponse;
import com.agro.feedmanagement.service.FeedManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/feeds")
@Tag(name = "Feed Management", description = "Gerenciamento de consumo de ração animal")
public class FeedManagementController {

	private final FeedManagementService service;

	private static final Logger log = LoggerFactory.getLogger(FeedManagementController.class);

	public FeedManagementController(FeedManagementService service) {
		this.service = service;
	}

	@Operation(summary = "Registrar consumo de ração", description = "Salva o consumo de ração de um animal e publica evento no RabbitMQ")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Consumo registrado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos na requisição") })
	@PostMapping
	public ResponseEntity<FeedResponse> createFeedRecord(@Valid @RequestBody FeedRequest request) {
		log.info("POST /feeds - animalId={}", request.animalId());
		FeedResponse response = service.createFeedRecord(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "Listar todos os consumos", description = "Retorna todos os registros de consumo de ração cadastrados")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso") })
	@GetMapping
	public ResponseEntity<List<FeedResponse>> getAllFeedRecords() {
		log.info("GET /feeds");
		return ResponseEntity.ok(service.getAllFeedRecords());
	}

	@Operation(summary = "Buscar consumos por animal", description = "Retorna todos os registros de consumo de um animal específico")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Registros encontrados"),
			@ApiResponse(responseCode = "404", description = "Nenhum registro encontrado para o animal") })
	@GetMapping("/{animalId}")
	public ResponseEntity<List<FeedResponse>> getFeedRecordsByAnimalId(@PathVariable String animalId) {
		log.info("GET /feeds/{}", animalId);
		return ResponseEntity.ok(service.getFeedRecordsByAnimalId(animalId));
	}
}