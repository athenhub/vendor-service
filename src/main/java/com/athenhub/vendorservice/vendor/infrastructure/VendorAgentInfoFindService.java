package com.athenhub.vendorservice.vendor.infrastructure;

import com.athenhub.vendorservice.vendor.domain.service.VendorAgentInfoFinder;
import com.athenhub.vendorservice.vendor.domain.vo.VendorAgent;
import com.athenhub.vendorservice.vendor.domain.vo.VendorAgentId;
import com.athenhub.vendorservice.vendor.infrastructure.client.MemberServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 업체 담당자(VendorAgent) 정보를 조회하는 서비스 구현체.
 *
 * <p>해당 서비스는 외부 회원 서비스(member-service)와 연동하여 업체 담당자 정보를 조회하고, 조회된 회원 정보를 기반으로 {@link VendorAgent}
 * 도메인 모델로 변환하여 반환한다.
 *
 * <h2>역할</h2>
 *
 * <ul>
 *   <li>회원 서비스에서 담당자 정보를 조회
 *   <li>조회된 회원 정보를 {@link VendorAgent}로 매핑
 * </ul>
 *
 * <p>구현체는 {@link VendorAgentInfoFinder} 인터페이스를 통해 사용되며, 업체 담당자 관련 기능에서 조회 책임을 담당한다.
 *
 * <p>{@link RequiredArgsConstructor}를 사용하여 필요한 의존성은 생성자 주입으로 관리된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class VendorAgentInfoFindService implements VendorAgentInfoFinder {

  private final MemberServiceClient memberServiceClient;

  @Override
  public VendorAgent find(VendorAgentId agentId) {
    return memberServiceClient.getMemberInfo(agentId.toUuid()).toVendorAgent();
  }
}
