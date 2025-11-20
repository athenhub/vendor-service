package com.athenhub.vendorservice.vendor.domain.service;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.vo.VendorId;

/**
 * 업체(Vendor) 조회를 담당하는 도메인 서비스 인터페이스.
 *
 * <p>업체 식별자 {@link VendorId}를 기반으로 등록된 업체 엔티티를 조회하며, 조회 실패 시 도메인 규칙에 따라 예외를 발생시킬 수 있다.
 *
 * <h2>역할</h2>
 *
 * <ul>
 *   <li>Vendor 단건 조회
 *   <li>존재하지 않는 경우 도메인 예외 처리
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface VendorFinder {
  /**
   * 업체를 단건 조회한다.
   *
   * @param vendorId 조회할 업체 식별자
   * @return 조회된 {@link Vendor} 엔티티
   */
  Vendor find(VendorId vendorId);
}
