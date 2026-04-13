package com.agro.feedcost.controller;

import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agro.feedcost.dto.FeedCostResponse;
import com.agro.feedcost.entity.FeedType;
import com.agro.feedcost.service.FeedCostService;

@RestController
@RequestMapping("/cost")
public class FeedCostController {

	private static final Logger log = Logger.getLogger(FeedCostController.class.getName());

	private final FeedCostService service;

	public FeedCostController(FeedCostService service) {
		this.service = service;
	}

	@GetMapping("/{feedType}")
	public ResponseEntity<FeedCostResponse> getCostByFeedType(@PathVariable FeedType feedType) {

		log.info("GET /cost/" + feedType);
		return ResponseEntity.ok(service.getCostByFeedType(feedType));

	}

}
