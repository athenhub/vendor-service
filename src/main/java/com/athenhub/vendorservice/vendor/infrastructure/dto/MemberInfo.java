package com.athenhub.vendorservice.vendor.infrastructure.dto;

import com.athenhub.vendorservice.vendor.domain.vo.VendorAgent;
import com.athenhub.vendorservice.vendor.domain.vo.VendorAgentId;
import com.athenhub.vendorservice.vendor.infrastructure.MemberRole;
import com.athenhub.vendorservice.vendor.infrastructure.MemberStatus;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 회원(Member) 정보를 나타내는 DTO.
 *
 * <p>외부 회원 서비스(member-service)로부터 조회되는 회원 상세 정보를 담는다.
 *
 * @param id 회원 식별자(UUID)
 * @param name 회원 이름
 * @param username 시스템 계정명
 * @param slackId Slack ID
 * @param role 회원 역할 {@link MemberRole}
 * @param status 계정 상태
 * @param organizationName 소속 조직명
 * @param organizationType 소속 타입
 * @param isActivated 활성화 여부
 * @author 김형섭
 * @since 1.0.0
 */
public record MemberInfo(
    UUID id,
    String name,
    String username,
    String slackId,
    MemberRole role,
    MemberStatus status,
    String organizationName,
    String organizationType,
    boolean isActivated) {
  /**
   * 회원 정보를 기반으로 {@link VendorAgent} 객체를 생성하여 반환한다.
   *
   * <p>해당 메서드는 회원 정보를 업체 담당자(VendorAgent) 도메인 모델로 변환할 때 사용된다.
   *
   * @return 변환된 {@link VendorAgent} 객체
   */
  public VendorAgent toVendorAgent() {
    return new VendorAgent(VendorAgentId.of(id), name, username, slackId);
  }
}
