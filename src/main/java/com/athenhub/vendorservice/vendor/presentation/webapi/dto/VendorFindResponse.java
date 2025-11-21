package com.athenhub.vendorservice.vendor.presentation.webapi.dto;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.VendorType;
import java.util.UUID;

/**
 * 업체 조회 응답 DTO.
 *
 * <p>특정 업체를 조회할 때 반환되는 응답 모델로, 업체의 기본 정보와 주소/좌표 정보를 포함한다. 본 레코드는 읽기 전용 구조이며, 컨트롤러 계층에서 API 응답 변환을
 * 위해 사용된다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>vendorId — 업체 식별자(UUID)
 *   <li>name — 업체명
 *   <li>type — 업체 유형({@link VendorType})
 *   <li>hubId — 소속 허브 식별자(UUID)
 *   <li>address — 주소
 *   <li>addressDetail — 상세 주소
 *   <li>latitude — 위도
 *   <li>longitude — 경도
 * </ul>
 *
 * <p>정적 메서드 {@link #from(Vendor)}를 통해 도메인 엔티티를 쉽게 응답 DTO로 변환할 수 있다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record VendorFindResponse(
    UUID vendorId,
    String name,
    VendorType type,
    UUID hubId,
    String address,
    String addressDetail,
    Double latitude,
    Double longitude) {

  /**
   * 도메인 엔티티 {@link Vendor}로부터 조회 응답 객체를 생성한다.
   *
   * @param vendor 조회된 업체 도메인 객체
   * @return {@link VendorFindResponse} 변환 결과
   */
  public static VendorFindResponse from(Vendor vendor) {
    return new VendorFindResponse(
        vendor.getId().toUuid(),
        vendor.getName(),
        vendor.getType(),
        vendor.getHubId().toUuid(),
        vendor.getAddress().getAddress(),
        vendor.getAddress().getDetailAddress(),
        vendor.getCoordinate().getLatitude(),
        vendor.getCoordinate().getLongitude());
  }
}
