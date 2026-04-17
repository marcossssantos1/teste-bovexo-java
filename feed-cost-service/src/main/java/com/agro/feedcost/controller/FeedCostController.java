package com.agro.feedcost.controller;

import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agro.feedcost.dto.FeedCostResponse;
import com.agro.feedcost.entity.FeedType;
import com.agro.feedcost.service.FeedCostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cost")
@Tag(name = "Feed Cost", description = "Consulta de custo dos insumos para alimentação animal")
public class FeedCostController {

	private static final Logger log = Logger.getLogger(FeedCostController.class.getName());

	private final FeedCostService service;

	public FeedCostController(FeedCostService service) {
		this.service = service;
	}

	@Operation(summary = "Buscar custo por tipo de ração", description = "Retorna o custo por kg de um insumo específico")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Custo retornado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Tipo de ração inválido"),
			@ApiResponse(responseCode = "404", description = "Insumo não encontrado") })
	@GetMapping("/{feedType}")
	public ResponseEntity<FeedCostResponse> getCostByFeedType(@PathVariable FeedType feedType) {

		log.info("GET /cost/" + feedType);
		return ResponseEntity.ok(service.getCostByFeedType(feedType));

	}

}
