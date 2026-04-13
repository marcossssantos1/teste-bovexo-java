package com.agro.feedmanagement.dto;

import com.agro.feedmanagement.entity.FeedType;

public record FeedEventMessage(
		String animalId,
        FeedType feedType,
        Double quantity
		) {

}
