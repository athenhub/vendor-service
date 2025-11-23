package com.athenhub.vendorservice.vendor.presentation.webapi.dto;

import com.athenhub.vendorservice.vendor.domain.vo.VendorAgent;
import java.util.UUID;

/**
 * 업체 담당자 조회 응답 DTO.
 *
 * <p>특정 업체 담당자를 조회할 때 반환되는 응답 모델로, 업체 담당자의 기본 정보를 포함한다. 본 레코드는 읽기 전용 구조이며, 컨트롤러 계층에서 API 응답 변환을 위해
 * 사용된다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>id — 업체 담당자 ID
 *   <li>name — 업체 담당자 이름
 *   <li>username — 업체 담당자 계정
 *   <li>slackId — 업체 담당자 슬랙 계정
 * </ul>
 *
 * <p>정적 메서드 {@link #of(VendorAgent)}를 통해 {@link VendorAgent}를 쉽게 응답 DTO로 변환할 수 있다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record VendorAgentResponse(UUID id, String name, String username, String slackId) {

  /**
   * {@link VendorAgent}로부터 조회 응답 객체를 생성한다.
   *
   * @param agent 조회된 업체 도메인 객체
   * @return {@link VendorAgentResponse} 변환 결과
   */
  public static VendorAgentResponse of(VendorAgent agent) {
    return new VendorAgentResponse(
        agent.id().toUuid(), agent.name(), agent.username(), agent.slackId());
  }
}
