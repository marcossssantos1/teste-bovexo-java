package com.agro.feedcost;

import com.agro.feedcost.dto.FeedCostDTO.FeedCostResponse;
import com.agro.feedcost.entity.FeedCost;
import com.agro.feedcost.entity.FeedType;
import com.agro.feedcost.exception.FeedCostNotFoundException;
import com.agro.feedcost.repository.FeedCostRepository;
import com.agro.feedcost.service.FeedCostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedCostServiceTest {

    @Mock
    private FeedCostRepository repository;

    @InjectMocks
    private FeedCostService service;

    @Test
    void deveRetornarCustoParaFeedTypeValido() {
        FeedCost feedCost = new FeedCost();
        feedCost.setFeedType(FeedType.MILHO);
        feedCost.setCostPerKg(new BigDecimal("2.50"));

        when(repository.findByFeedType(FeedType.MILHO)).thenReturn(Optional.of(feedCost));

        FeedCostResponse response = service.getCostByFeedType(FeedType.MILHO);

        assertThat(response.feedType()).isEqualTo(FeedType.MILHO);
        assertThat(response.costPerKg()).isEqualByComparingTo("2.50");
    }

    @Test
    void deveLancarExcecaoQuandoFeedTypeNaoEncontrado() {
        when(repository.findByFeedType(FeedType.UREIA)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCostByFeedType(FeedType.UREIA))
                .isInstanceOf(FeedCostNotFoundException.class)
                .hasMessageContaining("UREIA");
    }
}
