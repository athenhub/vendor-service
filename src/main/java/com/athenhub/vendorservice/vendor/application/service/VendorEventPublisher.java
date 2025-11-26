package com.athenhub.vendorservice.vendor.application.service;

import com.athenhub.vendorservice.vendor.domain.event.VendorAgentChanged;
import com.athenhub.vendorservice.vendor.domain.event.VendorDeleted;
import com.athenhub.vendorservice.vendor.domain.event.VendorRegistered;
import com.athenhub.vendorservice.vendor.domain.event.VendorUpdated;

/**
 * 업체(Vendor) 관련 도메인 이벤트를 발행(publish)하기 위한 인터페이스.
 *
 * <p>해당 인터페이스는 업체 생성, 수정, 삭제, 담당자 변경과 같은 주요 도메인 이벤트를 외부 메시지 브로커(Kafka, RabbitMQ 등) 또는 내부 이벤트 시스템으로
 * 전달하는 역할을 정의한다.
 *
 * <p>이 인터페이스는 구현체에서 실제 이벤트 브로커 연동 방식을 결정하도록 하며, 도메인 계층은 구현 방식에 영향을 받지 않고 이벤트 발행 기능을 사용할 수 있다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface VendorEventPublisher {
  /**
   * 신규 업체가 등록되었을 때 발행되는 이벤트를 전송한다.
   *
   * @param event 등록된 업체 정보를 담은 {@link VendorRegistered} 이벤트
   */
  void publish(VendorRegistered event);

  /**
   * 기존 업체의 정보가 수정되었을 때 발행되는 이벤트를 전송한다.
   *
   * @param event 수정된 업체 정보를 담은 {@link VendorUpdated} 이벤트
   */
  void publish(VendorUpdated event);

  /**
   * 업체가 삭제되었을 때 발행되는 이벤트를 전송한다.
   *
   * @param event 삭제된 업체 정보를 담은 {@link VendorDeleted} 이벤트
   */
  void publish(VendorDeleted event);

  /**
   * 업체 담당자(VendorAgent)가 변경되었을 때 발행되는 이벤트를 전송한다.
   *
   * @param event 변경된 담당자 정보를 담은 {@link VendorAgentChanged} 이벤트
   */
  void publish(VendorAgentChanged event);
}
