package com.agro.nutritionanalysis.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.agro.nutritionanalysis.document.NutritionAnalysis;

public interface NutritionAnalysisRepository extends MongoRepository<NutritionAnalysis, String> {

    List<NutritionAnalysis> findByAnimalId(String animalId);
}