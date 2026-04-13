package com.agro.feedmanagement.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "feed_record")
public class FeedRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "animal_id", nullable = false)
	private String animalId;

	@Enumerated(EnumType.STRING)
	@Column(name = "feed_type", nullable = false)
	private FeedType feedType;

	@Column(nullable = false)
	private Double quantity;

	@Column(name = "recorded_at", nullable = false)
	private LocalDateTime recordedAt;

	@PrePersist
	public void prePersist() {
		if (recordedAt == null) {
			recordedAt = LocalDateTime.now();
		}
	}

	public FeedRecord() {
	}

	public FeedRecord(Long id, String animalId, FeedType feedType, Double quantity, LocalDateTime recordedAt) {
		super();
		this.id = id;
		this.animalId = animalId;
		this.feedType = feedType;
		this.quantity = quantity;
		this.recordedAt = recordedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAnimalId() {
		return animalId;
	}

	public void setAnimalId(String animalId) {
		this.animalId = animalId;
	}

	public FeedType getFeedType() {
		return feedType;
	}

	public void setFeedType(FeedType feedType) {
		this.feedType = feedType;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public LocalDateTime getRecordedAt() {
		return recordedAt;
	}

	public void setRecordedAt(LocalDateTime recordedAt) {
		this.recordedAt = recordedAt;
	}
}