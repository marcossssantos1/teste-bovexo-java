package com.agro.feedmanagement.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.agro.feedmanagement.dto.FeedEventMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedEventProducer {

	private final RabbitTemplate rabbitTemplate;

	private static final Logger log = LoggerFactory.getLogger(FeedEventProducer.class);

	public FeedEventProducer(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Value("${rabbitmq.exchange.feed}")
	private String exchange;

	@Value("${rabbitmq.routing-key.feed}")
	private String routingKey;

	public void sendFeedEvent(FeedEventMessage event) {
		log.info("Publicando evento no RabbitMQ: animalId={}, feedType={}, quantity={}", event.animalId(),
				event.feedType(), event.quantity());

		rabbitTemplate.convertAndSend(exchange, routingKey, event);

		log.info("Evento publicado com sucesso para a fila.");
	}
}