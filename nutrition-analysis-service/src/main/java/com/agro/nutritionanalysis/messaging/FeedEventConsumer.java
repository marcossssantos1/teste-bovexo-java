package com.agro.nutritionanalysis.messaging;

import java.util.logging.Logger;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.agro.nutritionanalysis.dto.FeedEventMessage;
import com.agro.nutritionanalysis.service.NutritionAnalysisService;

@Component
public class FeedEventConsumer {

	private static final Logger log = Logger.getLogger(FeedEventConsumer.class.getName());

	private final NutritionAnalysisService service;

	public FeedEventConsumer(NutritionAnalysisService service) {
		this.service = service;
	}

	@RabbitListener(queues = "${rabbitmq.queue.feed}")
	public void consume(FeedEventMessage event) {
		log.info("Evento recebido do RabbitMQ: animalId=" + event.animalId());
		service.processarEvento(event);
	}
}