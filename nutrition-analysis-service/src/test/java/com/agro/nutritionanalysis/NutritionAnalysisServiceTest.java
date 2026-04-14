package com.agro.nutritionanalysis;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.agro.nutritionanalysis.document.FeedType;
import com.agro.nutritionanalysis.document.NutritionAnalysis;
import com.agro.nutritionanalysis.dto.FeedCostResponse;
import com.agro.nutritionanalysis.dto.FeedEventMessage;
import com.agro.nutritionanalysis.dto.NutritionAnalysisResponse;
import com.agro.nutritionanalysis.exception.NutritionAnalysisNotFoundException;
import com.agro.nutritionanalysis.repository.NutritionAnalysisRepository;
import com.agro.nutritionanalysis.service.NutritionAnalysisService;

@ExtendWith(MockitoExtension.class)
class NutritionAnalysisServiceTest {

    @Mock
    private NutritionAnalysisRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NutritionAnalysisService service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "feedCostServiceUrl", "http://localhost:8082");
    }

    @Test
    void deveProcessarEventoECalcularCustoTotal() {
        FeedEventMessage event = new FeedEventMessage("animal-01", FeedType.MILHO, 10.0);
        FeedCostResponse costResponse = new FeedCostResponse(FeedType.MILHO, new BigDecimal("2.50"));

        when(restTemplate.getForObject(contains("/cost/MILHO"), eq(FeedCostResponse.class)))
                .thenReturn(costResponse);

        NutritionAnalysis saved = new NutritionAnalysis();
        saved.setId("abc123");
        saved.setAnimalId("animal-01");
        saved.setFeedType(FeedType.MILHO);
        saved.setQuantity(10.0);
        saved.setCostPerKg(new BigDecimal("2.50"));
        saved.setTotalCost(new BigDecimal("25.00"));
        saved.setAnalysisDate(LocalDateTime.now());

        when(repository.save(any())).thenReturn(saved);

        service.processarEvento(event);

        verify(repository, times(1)).save(any(NutritionAnalysis.class));
        verify(restTemplate, times(1)).getForObject(contains("/cost/MILHO"), eq(FeedCostResponse.class));
    }

    @Test
    void deveLancarExcecaoQuandoAnimalNaoTemAnalise() {
        when(repository.findByAnimalId("inexistente")).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> service.getAnalysesByAnimalId("inexistente"))
                .isInstanceOf(NutritionAnalysisNotFoundException.class)
                .hasMessageContaining("inexistente");
    }

    @Test
    void deveRetornarTodasAsAnalises() {
        NutritionAnalysis analysis = new NutritionAnalysis();
        analysis.setId("abc123");
        analysis.setAnimalId("animal-01");
        analysis.setFeedType(FeedType.SOJA);
        analysis.setQuantity(5.0);
        analysis.setCostPerKg(new BigDecimal("3.80"));
        analysis.setTotalCost(new BigDecimal("19.00"));
        analysis.setAnalysisDate(LocalDateTime.now());

        when(repository.findAll()).thenReturn(List.of(analysis));

        List<NutritionAnalysisResponse> result = service.getAllAnalyses();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).totalCost()).isEqualByComparingTo("19.00");
    }
}
