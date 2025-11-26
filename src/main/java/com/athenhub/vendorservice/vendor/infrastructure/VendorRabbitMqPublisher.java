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

/**
 * RabbitMQ를 통해 업체 관련 도메인 이벤트를 발행하는 구현체.
 *
 * <p>각 이벤트는 {@link RabbitTemplate}을 사용해 지정된 익스체인지로 전달되며, 라우팅 키는 이벤트 종류에 따라 구분된다. 설정 정보는 {@link
 * RabbitProperties}를 통해 주입된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
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
