package com.agro.feedcost.service;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.agro.feedcost.dto.FeedCostResponse;
import com.agro.feedcost.entity.FeedCost;
import com.agro.feedcost.entity.FeedType;
import com.agro.feedcost.exception.FeedCostNotFoundException;
import com.agro.feedcost.repository.FeedCostRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedCostService {

	private static final Logger log = Logger.getLogger(FeedCostService.class.getName());

	private final FeedCostRepository repository;

	public FeedCostService(FeedCostRepository repository) {
		this.repository = repository;
	}
	
	@Transactional(readOnly = true)
	public FeedCostResponse getCostByFeedType(FeedType feedType) {
		log.info("Buscando custo para feedType=" + feedType);
		
		FeedCost feedCost = repository.findByFeedType(feedType).orElseThrow(() -> new FeedCostNotFoundException(feedType.name()));
		
		return new FeedCostResponse(feedCost.getFeedType(), feedCost.getCostPerKg());
	}

}
