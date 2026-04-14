package com.agro.nutritionanalysis.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "nutrition_analysis")
public class NutritionAnalysis {

	@Id
	private String id;

	private String animalId;
	private FeedType feedType;
	private Double quantity;
	private BigDecimal costPerKg;
	private BigDecimal totalCost;
	private LocalDateTime analysisDate;

	public NutritionAnalysis() {
	}

	public NutritionAnalysis(String id, String animalId, FeedType feedType, Double quantity, BigDecimal costPerKg,
			BigDecimal totalCost, LocalDateTime analysisDate) {
		super();
		this.id = id;
		this.animalId = animalId;
		this.feedType = feedType;
		this.quantity = quantity;
		this.costPerKg = costPerKg;
		this.totalCost = totalCost;
		this.analysisDate = analysisDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public BigDecimal getCostPerKg() {
		return costPerKg;
	}

	public void setCostPerKg(BigDecimal costPerKg) {
		this.costPerKg = costPerKg;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public LocalDateTime getAnalysisDate() {
		return analysisDate;
	}

	public void setAnalysisDate(LocalDateTime analysisDate) {
		this.analysisDate = analysisDate;
	}
}