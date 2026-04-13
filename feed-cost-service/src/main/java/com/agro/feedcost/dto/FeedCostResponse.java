package com.agro.feedcost.dto;

import java.math.BigDecimal;

import com.agro.feedcost.entity.FeedType;

public record FeedCostResponse(
		FeedType feedType, BigDecimal costPerKg
		) 
{}
