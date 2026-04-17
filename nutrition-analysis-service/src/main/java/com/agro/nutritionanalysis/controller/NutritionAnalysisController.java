package com.agro.nutritionanalysis.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agro.nutritionanalysis.dto.NutritionAnalysisResponse;
import com.agro.nutritionanalysis.service.NutritionAnalysisService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/analysis")
@Tag(name = "Nutrition Analysis", description = "Consulta de análises nutricionais e custos por animal")
public class NutritionAnalysisController {

	private static final Logger log = Logger.getLogger(NutritionAnalysisController.class.getName());

	private final NutritionAnalysisService service;

	public NutritionAnalysisController(NutritionAnalysisService service) {
		this.service = service;
	}

	@Operation(summary = "Listar todas as análises", description = "Retorna todas as análises nutricionais registradas")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso") })
	@GetMapping
	public ResponseEntity<List<NutritionAnalysisResponse>> getAllAnalyses() {
		log.info("GET /analysis");
		return ResponseEntity.ok(service.getAllAnalyses());
	}

	@Operation(summary = "Buscar análises por animal", description = "Retorna todas as análises nutricionais de um animal específico")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Análises encontradas"),
			@ApiResponse(responseCode = "404", description = "Nenhuma análise encontrada para o animal") })
	@GetMapping("/{animalId}")
	public ResponseEntity<List<NutritionAnalysisResponse>> getAnalysesByAnimalId(@PathVariable String animalId) {
		log.info("GET /analysis/" + animalId);
		return ResponseEntity.ok(service.getAnalysesByAnimalId(animalId));
	}
}
