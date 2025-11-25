package com.athenhub.vendorservice.vendor.domain.event;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import java.util.UUID;

/**
 * 신규 업체가 등록되었음을 나타하는 도메인 이벤트.
 *
 * <p>업체 ID, 업체명, 담당자 ID 등을 포함하며, 회원 서비스 또는 관련 외부 시스템에서 신규 등록 처리 시 활용된다.
 *
 * @param vendorId 등록된 업체 ID
 * @param vendorName 등록된 업체명
 * @param vendorAgentId 업체 담당자 ID
 * @param requestUsername 요청을 수행한 관리자 계정명
 */
public record VendorRegistered(
    UUID vendorId, String vendorName, UUID vendorAgentId, String requestUsername) {

  /**
   * 주어진 {@link Vendor} 엔티티로부터 {@code VendorRegistered} 이벤트 객체를 생성한다.
   *
   * @param vendor 등록된 업체 엔티티
   * @param requestUsername 요청을 수행한 관리자 계정명
   * @return 생성된 {@link VendorRegistered} 이벤트 객체
   */
  public static VendorRegistered from(Vendor vendor, String requestUsername) {
    return new VendorRegistered(
        vendor.getId().toUuid(), vendor.getName(), vendor.getAgentId().toUuid(), requestUsername);
  }
}
