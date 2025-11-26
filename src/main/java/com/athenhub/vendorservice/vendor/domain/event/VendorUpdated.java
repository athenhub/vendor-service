package com.athenhub.vendorservice.vendor.domain.event;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import java.util.UUID;

/**
 * 업체 정보가 수정되었음을 나타내는 도메인 이벤트.
 *
 * <p>업체의 이름 등이 변경되었을 때 발행된다.
 *
 * @param vendorName 변경된 업체명
 * @param vendorAgentId 업체 담당자 ID
 * @param requestUsername 요청을 수행한 관리자 계정명
 */
public record VendorUpdated(String vendorName, UUID vendorAgentId, String requestUsername) {

  /**
   * 주어진 {@link Vendor} 엔티티로부터 {@code VendorUpdated} 이벤트 객체를 생성한다.
   *
   * @param vendor 수정된 업체 엔티티
   * @param requestUsername 요청을 수행한 관리자 계정명
   * @return 생성된 {@link VendorUpdated} 이벤트 객체
   */
  public static VendorUpdated from(Vendor vendor, String requestUsername) {
    return new VendorUpdated(vendor.getName(), vendor.getAgentId().toUuid(), requestUsername);
  }
}
