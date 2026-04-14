package com.agro.nutritionanalysis.dto;

import com.agro.nutritionanalysis.document.FeedType;

public record FeedEventMessage(
		String animalId,
        FeedType feedType,
        Double quantity
		) {}
