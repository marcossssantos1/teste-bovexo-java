package com.agro.nutritionanalysis.exception;

public class NutritionAnalysisNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NutritionAnalysisNotFoundException(String animalId) {
        super("Nenhuma análise encontrada para o animal: " + animalId);
    }
}
