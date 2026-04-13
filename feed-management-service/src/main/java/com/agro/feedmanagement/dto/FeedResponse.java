package com.agro.feedmanagement.dto;

import java.time.LocalDateTime;

import com.agro.feedmanagement.entity.FeedType;

public record FeedResponse(
		Long id,
        String animalId,
        FeedType feedType,
        Double quantity,
        LocalDateTime recordedAt
		) {

}
