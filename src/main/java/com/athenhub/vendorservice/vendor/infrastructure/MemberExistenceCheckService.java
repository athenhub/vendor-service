package com.athenhub.vendorservice.vendor.infrastructure;

import com.athenhub.vendorservice.vendor.domain.service.HubExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.MemberExistenceChecker;
import com.athenhub.vendorservice.vendor.infrastructure.client.HubServiceClient;
import com.athenhub.vendorservice.vendor.infrastructure.client.MemberServiceClient;
import com.athenhub.vendorservice.vendor.infrastructure.dto.MemberInfo;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 회원 존재 여부를 확인하는 서비스 구현체.
 *
 * <p>이 서비스는 {@link HubServiceClient}를 통해 외부 Hub 서비스에 회원 정보를 조회하고, 회원이 실제 존재하며 삭제되지 않은 상태인지 판단한다.
 *
 * <p>도메인 계층에서 정의한 {@link HubExistenceChecker} 인터페이스의 구현체로, 애플리케이션 또는 도메인 서비스 계층에서 회원 유효성 검증을 위해
 * 사용된다.
 */
@Component
@RequiredArgsConstructor
public class MemberExistenceCheckService implements MemberExistenceChecker {

  private final MemberServiceClient memberServiceClient;

  /**
   * 주어진 회원 ID가 실제 존재하며 삭제되지 않은 유효한 회원인지 검사한다.
   *
   * <p>Hub 서비스에서 정보를 조회한 뒤, 해당 데이터가 존재하고 {@code isDeleted() == false}이면 유효한 회원으로 판단한다.
   *
   * @param memberId 존재 여부를 확인할 대상 회원 ID
   * @return 회원이 존재하고 삭제되지 않은 경우 {@code true}, 그렇지 않으면 {@code false}
   */
  @Override
  public boolean hasMember(UUID memberId) {
    MemberInfo member = memberServiceClient.getMemberInfo(memberId);
    return Objects.nonNull(member) && member.isActivated();
  }
}
