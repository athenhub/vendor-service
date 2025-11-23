package com.athenhub.vendorservice.vendor.domain;

import com.athenhub.vendorservice.global.domain.AbstractAuditEntity;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorRegisterRequest;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorUpdateRequest;
import com.athenhub.vendorservice.vendor.domain.exception.PermissionErrorCode;
import com.athenhub.vendorservice.vendor.domain.exception.PermissionException;
import com.athenhub.vendorservice.vendor.domain.service.HubExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.MemberExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.PermissionChecker;
import com.athenhub.vendorservice.vendor.domain.service.VendorAgentInfoFinder;
import com.athenhub.vendorservice.vendor.domain.vo.Address;
import com.athenhub.vendorservice.vendor.domain.vo.Coordinate;
import com.athenhub.vendorservice.vendor.domain.vo.HubId;
import com.athenhub.vendorservice.vendor.domain.vo.VendorAgent;
import com.athenhub.vendorservice.vendor.domain.vo.VendorAgentId;
import com.athenhub.vendorservice.vendor.domain.vo.VendorId;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

/**
 * 업체(Vendor) 도메인 엔티티.
 *
 * <p>업체 등록, 조회, 정보 수정, 삭제 기능을 담당하며, 감사(Audit) 기능은 상위 {@link AbstractAuditEntity}를 통해 처리된다.
 *
 * <p>권한 검증 및 허브 존재 여부 확인을 위해 {@link PermissionChecker}, {@link HubExistenceChecker}를 사용한다. 모든 등록 및
 * 수정, 삭제 시에는 도메인 규칙에 따라 권한과 허브 존재 여부가 검증되며, 위반 시 {@link PermissionException} 또는 {@link
 * IllegalArgumentException}이 발생한다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>id — 업체 식별자({@link VendorId})
 *   <li>name — 업체명
 *   <li>type — 업체 유형({@link VendorType})
 *   <li>hubId — 소속 허브 식별자({@link HubId})
 *   <li>address — 주소 정보({@link Address})
 *   <li>coordinate — 위치 정보({@link Coordinate})
 * </ul>
 *
 * <h2>주요 메서드</h2>
 *
 * <ul>
 *   <li>{@link #register(VendorRegisterRequest, PermissionChecker, HubExistenceChecker,
 *       MemberExistenceChecker, UUID)} — 새로운 업체 등록
 *   <li>{@link #updateInfo(VendorUpdateRequest, PermissionChecker, HubExistenceChecker, UUID)} — 업체
 *       정보 수정
 *   <li>{@link #delete(String, PermissionChecker, UUID)} — 업체 삭제
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Table(name = "p_vendor")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vendor extends AbstractAuditEntity {

  @EmbeddedId private VendorId id;

  private String name;

  @Enumerated(EnumType.STRING)
  private VendorType type;

  @Embedded private HubId hubId;

  @Embedded private Address address;

  @Embedded private Coordinate coordinate;

  @Embedded private VendorAgentId agentId;

  /**
   * 업체를 등록한다.
   *
   * <p>등록 시 요청 사용자의 권한과 허브 존재 여부를 검증하며, 검증 실패 시 {@link PermissionException} 또는 {@link
   * IllegalArgumentException}이 발생한다.
   *
   * @param registerRequest 등록 요청 데이터
   * @param permissionChecker 권한 검증 인터페이스
   * @param hubExistenceChecker 허브 존재 여부 검증 인터페이스
   * @param memberExistenceChecker 회원 존재 여부 검증 인터페이스
   * @param requestId 요청자 식별자(UUID)
   * @return 등록된 업체 엔티티
   * @throws NullPointerException 필수 입력 값이 누락된 경우
   * @throws PermissionException 등록 권한이 없는 경우
   * @throws IllegalArgumentException 허브 또는 멤버가 존재하지 않는 경우
   */
  public static Vendor register(
      VendorRegisterRequest registerRequest,
      PermissionChecker permissionChecker,
      HubExistenceChecker hubExistenceChecker,
      MemberExistenceChecker memberExistenceChecker,
      UUID requestId) {

    HubId hubId = HubId.of(registerRequest.hubId());

    checkRegisterPermission(hubId, permissionChecker, requestId);
    checkHubExistence(hubId, hubExistenceChecker);
    checkMemberExistence(registerRequest.agentId(), memberExistenceChecker);

    Vendor vendor = new Vendor();

    vendor.id = VendorId.generateId();
    vendor.name = Objects.requireNonNull(registerRequest.name());
    vendor.type = registerRequest.type();
    vendor.hubId = hubId;
    vendor.address = Address.of(registerRequest.streetAddress(), registerRequest.detailAddress());
    vendor.coordinate = Coordinate.of(registerRequest.latitude(), registerRequest.longitude());
    vendor.agentId = VendorAgentId.of(registerRequest.agentId());

    return vendor;
  }

  /**
   * 업체 정보를 수정한다.
   *
   * <p>수정 시 관리 권한과 허브 존재 여부를 검증하며, 검증 실패 시 {@link PermissionException} 또는 {@link
   * IllegalArgumentException}이 발생한다.
   *
   * @param updateRequest 수정 요청 데이터
   * @param permissionChecker 권한 검증 인터페이스
   * @param hubExistenceChecker 허브 존재 여부 검증 인터페이스
   * @param requestId 요청자 식별자(UUID)
   * @throws NullPointerException 필수 입력 값이 누락된 경우
   * @throws PermissionException 관리 권한이 없는 경우
   * @throws IllegalArgumentException 허브가 존재하지 않는 경우
   */
  public void updateInfo(
      VendorUpdateRequest updateRequest,
      PermissionChecker permissionChecker,
      HubExistenceChecker hubExistenceChecker,
      UUID requestId) {

    checkUpdatePermission(this.hubId, permissionChecker, requestId);
    checkHubExistence(HubId.of(updateRequest.hubId()), hubExistenceChecker);

    this.name = updateRequest.name();
    this.type = updateRequest.type();
    this.hubId = HubId.of(updateRequest.hubId());
    this.address = Address.of(updateRequest.streetAddress(), updateRequest.detailAddress());
    this.coordinate = Coordinate.of(updateRequest.latitude(), updateRequest.longitude());
  }

  /**
   * 업체를 삭제 처리한다.
   *
   * <p>삭제 시 관리 권한을 검증하며, 권한 부족 시 {@link PermissionException}이 발생한다.
   *
   * @param deletedBy 삭제 처리한 회원명
   * @param permissionChecker 권한 검증 인터페이스
   * @param requestId 요청자 식별자(UUID)
   * @throws PermissionException 관리 권한이 없는 경우
   */
  public void delete(String deletedBy, PermissionChecker permissionChecker, UUID requestId) {
    checkDeletePermission(this.hubId, permissionChecker, requestId);

    super.delete(deletedBy);
  }

  /**
   * 업체 담당자 정보를 조회한다.
   *
   * @param agentFinder 업체 담당자 정보 조회 인터페이스
   * @return 업체 담당자 정보
   */
  public VendorAgent getAgentInfo(VendorAgentInfoFinder agentFinder) {
    return agentFinder.find(this.agentId);
  }

  /**
   * 업체 담당자를 변경한다.
   *
   * @param newAgentId 새로운 업채 담당자 ID
   * @param permissionChecker 권한 검증 인터페이스
   * @param memberExistenceChecker 회원 존재 여부 검증 인터페이스
   * @param requestId 요청자 식별자(UUID)
   * @throws PermissionException 관리 권한이 없는 경우
   * @throws IllegalArgumentException 회원이 존재하지 않는 경우
   */
  public void changeAgent(
      UUID newAgentId,
      PermissionChecker permissionChecker,
      MemberExistenceChecker memberExistenceChecker,
      UUID requestId) {
    checkUpdatePermission(this.hubId, permissionChecker, requestId);
    checkMemberExistence(newAgentId, memberExistenceChecker);

    this.agentId = VendorAgentId.of(newAgentId);
  }

  private static void checkRegisterPermission(
      HubId hubId, PermissionChecker permissionChecker, UUID requestId) {
    if (!permissionChecker.hasRegisterPermission(requestId, hubId)) {
      throw new PermissionException(PermissionErrorCode.HAS_NOT_REGISTER_PERMISSION);
    }
  }

  private void checkUpdatePermission(
      HubId hubId, PermissionChecker permissionChecker, UUID requestId) {
    if (!permissionChecker.hasUpdatePermission(requestId, hubId, this.agentId)) {
      throw new PermissionException(PermissionErrorCode.HAS_NOT_UPDATE_PERMISSION);
    }
  }

  private void checkDeletePermission(
      HubId hubId, PermissionChecker permissionChecker, UUID requestId) {
    if (!permissionChecker.hasDeletePermission(requestId, hubId)) {
      throw new PermissionException(PermissionErrorCode.HAS_NOT_DELETE_PERMISSION);
    }
  }

  private static void checkHubExistence(HubId hubId, HubExistenceChecker hubExistenceChecker) {
    if (!hubExistenceChecker.hasHub(hubId)) {
      throw new IllegalArgumentException("허브가 존재하지 않습니다. id: " + hubId);
    }
  }

  private static void checkMemberExistence(
      UUID memberId, MemberExistenceChecker memberExistenceChecker) {
    if (!memberExistenceChecker.hasMember(memberId)) {
      throw new IllegalArgumentException("회원이 존재하지 않습니다. id: " + memberId);
    }
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass =
        o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    Vendor vendor = (Vendor) o;
    return getId() != null && Objects.equals(getId(), vendor.getId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(id);
  }
}
