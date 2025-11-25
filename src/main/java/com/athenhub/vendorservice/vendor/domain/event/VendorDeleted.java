package com.athenhub.vendorservice.vendor.domain.event;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import java.util.UUID;

/**
 * 업체가 삭제되었음을 나타내는 도메인 이벤트.
 *
 * <p>업체 ID는 포함되지 않으며, 업체 담당자 ID와 요청 사용자 정보만 포함된다. 외부 시스템에서는 업체 관련 캐시, 권한 정보 등을 정리하는 용도로 사용할 수 있다.
 *
 * @param vendorAgentId 삭제된 업체의 담당자 ID
 * @param requestUsername 요청을 수행한 관리자 계정명
 */
public record VendorDeleted(UUID vendorAgentId, String requestUsername) {

  /**
   * 주어진 {@link Vendor} 엔티티로부터 {@code VendorDeleted} 이벤트 객체를 생성한다.
   *
   * @param vendor 삭제된 업체 엔티티
   * @param requestUsername 요청을 수행한 관리자 계정명
   * @return 생성된 {@link VendorDeleted} 이벤트 객체
   */
  public static VendorDeleted from(Vendor vendor, String requestUsername) {
    return new VendorDeleted(vendor.getAgentId().toUuid(), requestUsername);
  }
}
