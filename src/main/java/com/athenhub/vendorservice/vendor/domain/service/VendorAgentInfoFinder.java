package com.athenhub.vendorservice.vendor.domain.service;

import com.athenhub.vendorservice.vendor.domain.vo.VendorAgent;
import com.athenhub.vendorservice.vendor.domain.vo.VendorAgentId;

/**
 * 업체 담당자(VendorAgent) 정보를 조회하는 기능을 제공하는 조회 전용 인터페이스.
 *
 * <p>구현체는 저장소 또는 외부 서비스에서 담당자 정보를 조회하는 책임을 가진다. 일반적으로 읽기(Read) 전용 유스케이스에서 사용된다.
 *
 * <h2>역할</h2>
 *
 * <ul>
 *   <li>담당자 ID를 기반으로 담당자 정보를 조회
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface VendorAgentInfoFinder {
  /**
   * 담당자 ID에 해당하는 업체 담당자 정보를 조회한다.
   *
   * @param agentId 조회할 담당자 ID
   * @return 조회된 {@link VendorAgent} 정보
   */
  VendorAgent find(VendorAgentId agentId);
}
