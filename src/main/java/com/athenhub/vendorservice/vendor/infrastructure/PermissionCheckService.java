package com.athenhub.vendorservice.vendor.infrastructure;

import com.athenhub.vendorservice.vendor.domain.service.PermissionChecker;
import com.athenhub.vendorservice.vendor.domain.vo.HubId;
import com.athenhub.vendorservice.vendor.infrastructure.client.HubServiceClient;
import com.athenhub.vendorservice.vendor.infrastructure.client.MemberServiceClient;
import com.athenhub.vendorservice.vendor.infrastructure.dto.HubManager;
import com.athenhub.vendorservice.vendor.infrastructure.dto.MemberInfo;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 권한 검증을 담당하는 서비스 구현체.
 *
 * <p>{@link PermissionChecker} 인터페이스를 구현하여, 요청 사용자가 특정 허브에 대한 관리 권한을 가지고 있는지 여부를 검증한다.
 *
 * <p>주요 역할:
 *
 * <ul>
 *   <li>요청자의 역할과 허브 매니저 여부를 기반으로 관리 권한 판단
 *   <li>Master Manager 또는 해당 허브의 Hub Manager인지 확인
 * </ul>
 *
 * <p>외부 시스템 의존:
 *
 * <ul>
 *   <li>{@link MemberServiceClient} — 요청자의 회원 정보 조회
 *   <li>{@link HubServiceClient} — 허브 정보 및 관리자를 확인
 * </ul>
 *
 * <p>생성자 주입은 {@link RequiredArgsConstructor}를 통해 자동 처리된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class PermissionCheckService implements PermissionChecker {

  private final MemberServiceClient memberServiceClient;
  private final HubServiceClient hubServiceClient;

  @Override
  public boolean hasRegisterPermission(UUID requestId, HubId hubId) {
    MemberInfo member = memberServiceClient.getMemberInfo(requestId);

    return isActiveMasterManager(member) || isActiveManagerOfHub(member, hubId, requestId);
  }

  @Override
  public boolean hasManagePermission(UUID requestId, HubId hubId) {
    return hasRegisterPermission(requestId, hubId);
  }

  /**
   * 요청자가 해당 허브의 활성 Hub Manager인지 여부를 검증한다.
   *
   * @param member 회원 정보 객체
   * @param hubId 검증 대상 허브 식별자
   * @return 해당 허브의 관리자이면 {@code true}, 아니면 {@code false}
   */
  private boolean isActiveManagerOfHub(MemberInfo member, HubId hubId, UUID requestId) {
    if (!isActiveHubManager(member)) {
      return false;
    }

    HubManager manager = hubServiceClient.getHubManager(hubId.toUuid());

    return requestId.equals(manager.id());
  }

  /**
   * 요청자가 활성 상태의 Hub Manager 역할인지 확인한다.
   *
   * <p>Hub Manager 역할이며, 논리적으로 삭제되지 않은(deletedAt 값이 없는) 경우에만 활성 사용자로 판단한다.
   *
   * @param member 회원 정보 객체
   * @return 활성 Hub Manager이면 {@code true}, 아니면 {@code false}
   */
  private boolean isActiveHubManager(MemberInfo member) {
    return MemberRole.HUB_MANAGER.equals(member.role()) && Objects.isNull(member.deletedAt());
  }

  /**
   * 요청자가 활성 상태의 Master Manager 역할인지 확인한다.
   *
   * @param member 회원 정보 객체
   * @return 활성 Master Manager이면 {@code true}, 아니면 {@code false}
   */
  private boolean isActiveMasterManager(MemberInfo member) {
    return MemberRole.MASTER_MANAGER.equals(member.role()) && Objects.isNull(member.deletedAt());
  }
}
