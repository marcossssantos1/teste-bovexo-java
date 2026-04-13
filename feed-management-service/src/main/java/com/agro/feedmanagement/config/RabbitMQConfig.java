package com.agro.feedmanagement.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Value("${rabbitmq.queue.feed}")
	private String queueName;

	@Value("${rabbitmq.exchange.feed}")
	private String exchangeName;

	@Value("${rabbitmq.routing-key.feed}")
	private String routingKey;

	@Bean
	public Queue feedQueue() {
		return new Queue(queueName, true); 
	}

	@Bean
	public DirectExchange feedExchange() {
		return new DirectExchange(exchangeName);
	}

	@Bean
	public Binding feedBinding(Queue feedQueue, DirectExchange feedExchange) {
		return BindingBuilder.bind(feedQueue).to(feedExchange).with(routingKey);
	}

	@Bean
	public Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(messageConverter());
		return template;
	}
}