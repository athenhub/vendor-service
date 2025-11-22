package com.athenhub.vendorservice.vendor.infrastructure.client;

import com.athenhub.vendorservice.vendor.infrastructure.dto.HubManagers;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Hub 관련 외부 서비스 호출을 위한 Feign 클라이언트.
 *
 * <p>허브 서비스(hub-service)와 통신하여 특정 허브의 관리자 정보를 조회한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@FeignClient("hub-service")
public interface HubServiceClient {
  /**
   * 지정된 허브의 관리자 정보를 조회한다.
   *
   * @param hubId 허브 식별자(UUID)
   * @return 허브 관리자 정보 객체 {@link HubManagers}
   */
  @GetMapping("v1/hubs/{hubId}/managers")
  HubManagers getHubInfo(@PathVariable("hubId") UUID hubId);
}
