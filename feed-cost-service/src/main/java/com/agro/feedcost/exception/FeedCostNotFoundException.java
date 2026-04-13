package com.agro.feedcost.exception;

public class FeedCostNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public FeedCostNotFoundException(String feedType) {
        super("Custo não encontrado para o insumo: " + feedType);
    }
}