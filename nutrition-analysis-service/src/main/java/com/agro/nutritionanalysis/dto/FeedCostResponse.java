package com.agro.nutritionanalysis.dto;

import java.math.BigDecimal;

import com.agro.nutritionanalysis.document.FeedType;

public record FeedCostResponse(
		FeedType feedType,
        BigDecimal costPerKg
		) {}
