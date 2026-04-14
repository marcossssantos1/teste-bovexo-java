package com.agro.nutritionanalysis.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.agro.nutritionanalysis.document.NutritionAnalysis;
import com.agro.nutritionanalysis.dto.FeedCostResponse;
import com.agro.nutritionanalysis.dto.FeedEventMessage;
import com.agro.nutritionanalysis.dto.NutritionAnalysisResponse;
import com.agro.nutritionanalysis.exception.NutritionAnalysisNotFoundException;
import com.agro.nutritionanalysis.repository.NutritionAnalysisRepository;

@Service
public class NutritionAnalysisService {

	private static final Logger log = Logger.getLogger(NutritionAnalysisService.class.getName());

	private final NutritionAnalysisRepository repository;
	private final RestTemplate restTemplate;

	@Value("${feedcost.service.url}")
	private String feedCostServiceUrl;

	public NutritionAnalysisService(NutritionAnalysisRepository repository, RestTemplate restTemplate) {
		this.repository = repository;
		this.restTemplate = restTemplate;
	}

	public void processarEvento(FeedEventMessage event) {
		log.info("Processando evento: animalId=" + event.animalId() + ", feedType=" + event.feedType() + ", quantity="
				+ event.quantity());

		String url = feedCostServiceUrl + "/cost/" + event.feedType().name();
		FeedCostResponse feedCost = restTemplate.getForObject(url, FeedCostResponse.class);

		if (feedCost == null) {
			log.warning("Resposta nula do feed-cost-service para feedType=" + event.feedType());
			throw new RuntimeException("Não foi possível obter o custo para: " + event.feedType());
		}

		BigDecimal totalCost = feedCost.costPerKg().multiply(BigDecimal.valueOf(event.quantity())).setScale(2,
				RoundingMode.HALF_UP);

		NutritionAnalysis analysis = new NutritionAnalysis();
		analysis.setAnimalId(event.animalId());
		analysis.setFeedType(event.feedType());
		analysis.setQuantity(event.quantity());
		analysis.setCostPerKg(feedCost.costPerKg());
		analysis.setTotalCost(totalCost);
		analysis.setAnalysisDate(LocalDateTime.now());

		repository.save(analysis);
		log.info("Análise salva no MongoDB. totalCost=" + totalCost);
	}

	public List<NutritionAnalysisResponse> getAllAnalyses() {
		return repository.findAll().stream().map(this::toResponse).toList();
	}

	public List<NutritionAnalysisResponse> getAnalysesByAnimalId(String animalId) {
		List<NutritionAnalysis> analyses = repository.findByAnimalId(animalId);

		if (analyses.isEmpty()) {
			throw new NutritionAnalysisNotFoundException(animalId);
		}

		return analyses.stream().map(this::toResponse).toList();
	}

	private NutritionAnalysisResponse toResponse(NutritionAnalysis analysis) {
		return new NutritionAnalysisResponse(analysis.getId(), analysis.getAnimalId(), analysis.getFeedType(),
				analysis.getQuantity(), analysis.getCostPerKg(), analysis.getTotalCost(), analysis.getAnalysisDate());
	}
}