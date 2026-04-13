package com.agro.feedcost.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "feed_cost")
public class FeedCost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "feed_type", nullable = false, unique = true)
	private FeedType feedType;

	@Column(name = "cost_per_kg", nullable = false, precision = 10, scale = 2)
	private BigDecimal costPerKg;

	@Column(name = "last_update")
	private LocalDateTime lastUpdate;

	@PrePersist
	@PreUpdate
	public void prePersist() {
		lastUpdate = LocalDateTime.now();
	}

	public FeedCost() {
	}

	public FeedCost(Long id, FeedType feedType, BigDecimal costPerKg, LocalDateTime lastUpdate) {
		super();
		this.id = id;
		this.feedType = feedType;
		this.costPerKg = costPerKg;
		this.lastUpdate = lastUpdate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FeedType getFeedType() {
		return feedType;
	}

	public void setFeedType(FeedType feedType) {
		this.feedType = feedType;
	}

	public BigDecimal getCostPerKg() {
		return costPerKg;
	}

	public void setCostPerKg(BigDecimal costPerKg) {
		this.costPerKg = costPerKg;
	}

	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
