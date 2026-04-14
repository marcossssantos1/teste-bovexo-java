package com.agro.nutritionanalysis.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.agro.nutritionanalysis.document.FeedType;

public record NutritionAnalysisResponse(
		String id,
        String animalId,
        FeedType feedType,
        Double quantity,
        BigDecimal costPerKg,
        BigDecimal totalCost,
        LocalDateTime analysisDate
		) {}
