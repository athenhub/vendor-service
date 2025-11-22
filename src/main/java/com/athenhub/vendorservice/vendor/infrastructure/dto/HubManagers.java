package com.athenhub.vendorservice.vendor.infrastructure.dto;

import java.util.List;
import java.util.UUID;

/**
 * 특정 허브의 관리자 목록을 나타내는 DTO.
 *
 * <p>허브에 속한 모든 관리자 정보를 {@link HubManager} 리스트로 관리하며, 특정 사용자가 허브 관리자 여부인지 확인하는 유틸리티 메서드를 제공한다.
 *
 * @param hubManagers 허브 관리자 목록
 * @author 김형섭
 * @since 1.0.0
 */
public record HubManagers(List<HubManager> hubManagers) {
  /**
   * 주어진 ID가 허브 관리자 목록에 포함되는지 여부를 확인한다.
   *
   * @param id 확인할 관리자 ID(UUID)
   * @return 목록에 존재하면 {@code true}, 없으면 {@code false}
   */
  public boolean isHubManager(UUID id) {
    return hubManagers.stream().anyMatch(h -> h.id().equals(id));
  }
}
