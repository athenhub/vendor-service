package com.athenhub.vendorservice.vendor.domain.event;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import java.util.UUID;

/**
 * 업체 담당자(VendorAgent)가 변경되었음을 나타내는 도메인 이벤트.
 *
 * <p>업체 ID, 업체명, 이전 담당자 ID, 새로운 담당자 ID 등을 포함한다. 회원 서비스나 권한 시스템에서 관련 정보 동기화에 사용될 수 있다.
 *
 * @param vendorId 업체 ID
 * @param vendorName 업체명
 * @param oldVendorAgentId 기존 담당자 ID
 * @param newVendorAgentId 신규 담당자 ID
 * @param requestUsername 요청을 수행한 관리자 계정명
 */
public record VendorAgentChanged(
    UUID vendorId,
    String vendorName,
    UUID oldVendorAgentId,
    UUID newVendorAgentId,
    String requestUsername) {
  /**
   * 주어진 {@link Vendor} 엔티티로부터 {@code VendorAgentChanged} 이벤트 객체를 생성한다.
   *
   * @param vendor 담당자가 변경된 업체 엔티티
   * @param oldVendorAgentId 변경 이전 업체 담당자 ID
   * @param requestUsername 요청을 수행한 관리자 계정명
   * @return 생성된 {@link VendorAgentChanged} 이벤트 객체
   */
  public static VendorAgentChanged from(
      Vendor vendor, UUID oldVendorAgentId, String requestUsername) {
    return new VendorAgentChanged(
        vendor.getId().toUuid(),
        vendor.getName(),
        oldVendorAgentId,
        vendor.getAgentId().toUuid(),
        requestUsername);
  }
}
