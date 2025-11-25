package com.athenhub.vendorservice.vendor.infrastructure;

import com.athenhub.vendorservice.global.infrastructure.message.RabbitProperties;
import com.athenhub.vendorservice.vendor.application.service.VendorEventPublisher;
import com.athenhub.vendorservice.vendor.domain.event.VendorAgentChanged;
import com.athenhub.vendorservice.vendor.domain.event.VendorDeleted;
import com.athenhub.vendorservice.vendor.domain.event.VendorRegistered;
import com.athenhub.vendorservice.vendor.domain.event.VendorUpdated;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitProperties.class)
public class VendorRabbitMqPublisher implements VendorEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final RabbitProperties rabbitProperties;

  @Override
  public void publish(VendorRegistered event) {
    rabbitTemplate.convertAndSend(rabbitProperties.getExchange(), "registered", event);
  }

  @Override
  public void publish(VendorUpdated event) {
    rabbitTemplate.convertAndSend(rabbitProperties.getExchange(), "updated", event);
  }

  @Override
  public void publish(VendorDeleted event) {
    rabbitTemplate.convertAndSend(rabbitProperties.getExchange(), "deleted", event);
  }

  @Override
  public void publish(VendorAgentChanged event) {
    rabbitTemplate.convertAndSend(rabbitProperties.getExchange(), "agentChanged", event);
  }
}
