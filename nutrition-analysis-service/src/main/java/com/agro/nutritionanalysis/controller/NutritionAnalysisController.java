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

@RestController
@RequestMapping("/analysis")
public class NutritionAnalysisController {

    private static final Logger log = Logger.getLogger(NutritionAnalysisController.class.getName());

    private final NutritionAnalysisService service;

    public NutritionAnalysisController(NutritionAnalysisService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<NutritionAnalysisResponse>> getAllAnalyses() {
        log.info("GET /analysis");
        return ResponseEntity.ok(service.getAllAnalyses());
    }

    @GetMapping("/{animalId}")
    public ResponseEntity<List<NutritionAnalysisResponse>> getAnalysesByAnimalId(@PathVariable String animalId) {
        log.info("GET /analysis/" + animalId);
        return ResponseEntity.ok(service.getAnalysesByAnimalId(animalId));
    }
}
