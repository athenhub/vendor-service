package com.athenhub.vendorservice.vendor.presentation.webapi.dto;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import java.util.UUID;

/**
 * 업체 수정 응답 DTO.
 *
 * <p>업체 수정 요청 처리 후, 수정된 업체의 식별자 정보를 클라이언트에 반환하기 위한 응답 전용 레코드 타입이다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>vendorId — 수정된 업체의 식별자(UUID)
 * </ul>
 *
 * <p>{@link #from(Vendor)} 정적 팩터리 메서드를 통해 도메인 엔티티 {@link Vendor}로부터 쉽게 응답 객체를 생성할 수 있다.
 *
 * @param vendorId 수정된 업체의 UUID
 * @author 김형섭
 * @since 1.0.0
 */
public record VendorUpdateResponse(UUID vendorId) {
  /**
   * 도메인 엔티티 {@link Vendor}로부터 응답 객체를 생성한다.
   *
   * @param vendor 수정된 업체 도메인 객체
   * @return {@link VendorUpdateResponse} 생성 결과
   */
  public static VendorUpdateResponse from(Vendor vendor) {
    return new VendorUpdateResponse(vendor.getId().toUuid());
  }
}
