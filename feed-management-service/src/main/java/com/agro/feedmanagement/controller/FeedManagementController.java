package com.agro.feedmanagement.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agro.feedmanagement.dto.FeedRequest;
import com.agro.feedmanagement.dto.FeedResponse;
import com.agro.feedmanagement.service.FeedManagementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/feeds")
public class FeedManagementController {

	private final FeedManagementService service;

	private static final Logger log = LoggerFactory.getLogger(FeedManagementController.class);

	public FeedManagementController(FeedManagementService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<FeedResponse> createFeedRecord(@Valid @RequestBody FeedRequest request) {
		log.info("POST /feeds - animalId={}", request.animalId());
		FeedResponse response = service.createFeedRecord(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<List<FeedResponse>> getAllFeedRecords() {
		log.info("GET /feeds");
		return ResponseEntity.ok(service.getAllFeedRecords());
	}

	@GetMapping("/{animalId}")
	public ResponseEntity<List<FeedResponse>> getFeedRecordsByAnimalId(@PathVariable String animalId) {
		log.info("GET /feeds/{}", animalId);
		return ResponseEntity.ok(service.getFeedRecordsByAnimalId(animalId));
	}
}