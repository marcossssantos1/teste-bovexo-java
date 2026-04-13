package com.agro.feedmanagement.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agro.feedmanagement.controller.FeedManagementController;
import com.agro.feedmanagement.dto.FeedEventMessage;
import com.agro.feedmanagement.dto.FeedRequest;
import com.agro.feedmanagement.dto.FeedResponse;
import com.agro.feedmanagement.entity.FeedRecord;
import com.agro.feedmanagement.exception.FeedRecordNotFoundException;
import com.agro.feedmanagement.messaging.FeedEventProducer;
import com.agro.feedmanagement.repository.FeedRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedManagementService {

	private final FeedRecordRepository repository;
	private final FeedEventProducer eventProducer;

	private static final Logger log = LoggerFactory.getLogger(FeedManagementController.class);

	public FeedManagementService(FeedRecordRepository repository, FeedEventProducer eventProducer) {
		super();
		this.repository = repository;
		this.eventProducer = eventProducer;
	}

	@Transactional
	public FeedResponse createFeedRecord(FeedRequest request) {
		log.info("Criando registro de consumo para animalId={}", request.animalId());

		FeedRecord record = new FeedRecord();
		record.setAnimalId(request.animalId());
		record.setFeedType(request.feedType());
		record.setQuantity(request.quantity());
		record.setRecordedAt(request.recordedAt());

		FeedRecord saved = repository.save(record);
		log.info("Registro salvo com id={}", saved.getId());

		eventProducer
				.sendFeedEvent(new FeedEventMessage(saved.getAnimalId(), saved.getFeedType(), saved.getQuantity()));

		return toResponse(saved);
	}

	@Transactional(readOnly = true)
	public List<FeedResponse> getAllFeedRecords() {
		return repository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public List<FeedResponse> getFeedRecordsByAnimalId(String animalId) {
		List<FeedRecord> records = repository.findByAnimalId(animalId);

		if (records.isEmpty()) {
			throw new FeedRecordNotFoundException("Nenhum registro encontrado para o animal: " + animalId);
		}

		return records.stream().map(this::toResponse).toList();
	}

	private FeedResponse toResponse(FeedRecord record) {
		return new FeedResponse(record.getId(), record.getAnimalId(), record.getFeedType(), record.getQuantity(),
				record.getRecordedAt());
	}
}