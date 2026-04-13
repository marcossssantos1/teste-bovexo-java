package com.agro.feedmanagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.agro.feedmanagement.dto.FeedRequest;
import com.agro.feedmanagement.dto.FeedResponse;
import com.agro.feedmanagement.entity.FeedRecord;
import com.agro.feedmanagement.entity.FeedType;
import com.agro.feedmanagement.exception.FeedRecordNotFoundException;
import com.agro.feedmanagement.messaging.FeedEventProducer;
import com.agro.feedmanagement.repository.FeedRecordRepository;
import com.agro.feedmanagement.service.FeedManagementService;

@ExtendWith(MockitoExtension.class)
class FeedManagementServiceTest {

	@Mock
	private FeedRecordRepository repository;

	@Mock
	private FeedEventProducer eventProducer;

	@InjectMocks
	private FeedManagementService service;

	@Test
	void deveRetornarTodosOsRegistros() {
		FeedRecord record = new FeedRecord();
		record.setId(1L);
		record.setAnimalId("animal-01");
		record.setFeedType(FeedType.SOJA);
		record.setQuantity(5.0);
		record.setRecordedAt(LocalDateTime.now());

		when(repository.findAll()).thenReturn(List.of(record));

		List<FeedResponse> result = service.getAllFeedRecords();

		assertThat(result).hasSize(1);
		assertThat(result.get(0).feedType()).isEqualTo(FeedType.SOJA);
	}

	@Test
	void deveLancarExcecaoQuandoAnimalNaoEncontrado() {
		when(repository.findByAnimalId("inexistente")).thenReturn(Collections.emptyList());

		assertThatThrownBy(() -> service.getFeedRecordsByAnimalId("inexistente"))
				.isInstanceOf(FeedRecordNotFoundException.class).hasMessageContaining("inexistente");
	}

	@Test
	void deveCriarRegistroEPublicarEvento() {
		FeedRequest request = new FeedRequest("animal-01", FeedType.MILHO, 10.0, null);

		FeedRecord saved = new FeedRecord();
		saved.setId(1L);
		saved.setAnimalId("animal-01");
		saved.setFeedType(FeedType.MILHO);
		saved.setQuantity(10.0);
		saved.setRecordedAt(LocalDateTime.now());

		when(repository.save(any())).thenReturn(saved);
		doNothing().when(eventProducer).sendFeedEvent(any());

		FeedResponse response = service.createFeedRecord(request);

		assertThat(response.animalId()).isEqualTo("animal-01");
		assertThat(response.feedType()).isEqualTo(FeedType.MILHO);
		assertThat(response.quantity()).isEqualTo(10.0);
		verify(eventProducer, times(1)).sendFeedEvent(any());
	}
}