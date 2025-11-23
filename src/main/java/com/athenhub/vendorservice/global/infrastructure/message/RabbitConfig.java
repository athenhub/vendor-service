package com.athenhub.vendorservice.global.infrastructure.message;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
public class RabbitConfig {

  @Value("${rabbit.vendor.exchange}")
  private String exchange;

  @Value("${rabbit.vendor.queue}")
  private String queue;

  @Value("${rabbit.vendor.routing-key}")
  private String routingKey;

  @Bean
  public TopicExchange vendorExchange() {
    return new TopicExchange(exchange, true, false);
  }

  @Bean
  public Queue vendorQueue() {
    return QueueBuilder.durable(queue).build();
  }

  @Bean
  public Binding vendorBinding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(routingKey);
  }

  @Bean
  public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
