package com.athenhub.vendorservice.vendor.domain;

import com.athenhub.vendorservice.global.domain.AbstractAuditEntity;
import com.athenhub.vendorservice.vendor.domain.vo.Address;
import com.athenhub.vendorservice.vendor.domain.vo.HubId;
import com.athenhub.vendorservice.vendor.domain.vo.Location;
import com.athenhub.vendorservice.vendor.domain.vo.VendorId;
import com.athenhub.vendorservice.vendor.domain.vo.request.VendorRegisterRequest;
import com.athenhub.vendorservice.vendor.domain.vo.request.VendorUpdateRequest;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 업체(Vendor) 도메인을 표현하는 JPA 엔티티.
 *
 * <p>본 엔티티는 시스템 내에서 관리되는 업체 정보를 저장하며, 다음과 같은 주요 속성을 포함한다.
 *
 * <ul>
 *   <li>{@link VendorId} — 업체 식별자(복합 키), 엔티티의 식별자로 사용됨
 *   <li>{@code name} — 업체명
 *   <li>{@link VendorType} — 업체 유형(예: 배송업체, 물류업체 등)
 *   <li>{@link HubId} — 소속 허브 식별자
 *   <li>{@link Address} — 업체 주소 정보(주소, 상세주소 포함)
 *   <li>{@link Location} — 업체 위치 정보(위도/경도)
 * </ul>
 *
 * <p>{@code Vendor}는 {@link AbstractAuditEntity}를 상속하여 생성일시, 수정일시, 생성자, 수정자 등의 감사(auditing) 정보를
 * 자동으로 관리한다.
 *
 * <h2>생성 및 변경 규칙</h2>
 *
 * <p>도메인의 일관성을 보장하기 위해 생성과 변경 동작은 다음 메서드를 통해서만 수행된다.
 *
 * <ul>
 *   <li>{@link #register(VendorRegisterRequest)} — 신규 업체 등록 팩토리 메서드
 *   <li>{@link #updateInfo(VendorUpdateRequest)} — 기존 업체 정보 수정 메서드
 * </ul>
 *
 * <h3>사용 예시:</h3>
 *
 * <pre>{@code
 * VendorRegisterRequest request = new VendorRegisterRequest(
 *     "업체명",
 *     VendorType.RECEIVER,
 *     "HUB0001",
 *     "서울시 송파구 ...",
 *     "301호",
 *     37.1234,
 *     127.5678
 * );
 *
 * Vendor vendor = Vendor.register(request);
 * }</pre>
 *
 * <p>엔티티는 JPA 프록시 생성을 위해 기본 생성자를 {@code PROTECTED}로 유지한다.
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

  @Embedded private Location location;

  /**
   * 신규 업체(Vendor)를 등록하기 위한 팩토리 메서드.
   *
   * <p>필수 값은 {@code request} 내부에서 유효성 검증되며, {@link VendorId#generateId()} 를 통해 새로운 업체 식별자를 생성한다.
   *
   * @param request 업체 생성 요청 정보
   * @return 생성된 {@link Vendor} 엔티티
   * @throws NullPointerException 필수 입력 값이 누락된 경우
   */
  public static Vendor register(VendorRegisterRequest request) {
    Vendor vendor = new Vendor();

    vendor.id = VendorId.generateId();
    vendor.name = Objects.requireNonNull(request.name());
    vendor.type = request.type();
    vendor.hubId = HubId.of(request.hubId());
    vendor.address = Address.of(request.address(), request.detailAddress());
    vendor.location = Location.of(request.latitude(), request.longitude());

    return vendor;
  }

  /**
   * 기존 업체 정보를 업데이트한다.
   *
   * <p>업데이트 가능한 항목은 다음과 같다.
   *
   * <ul>
   *   <li>업체명
   *   <li>업체 유형
   *   <li>허브 ID
   *   <li>주소/상세주소
   *   <li>위치 정보(위도/경도)
   * </ul>
   *
   * @param request 업체 수정 요청 객체
   * @throws NullPointerException 필수 값이 누락된 경우
   */
  public void updateInfo(VendorUpdateRequest request) {
    this.name = request.name();
    this.type = request.type();
    this.hubId = HubId.of(request.hubId());
    this.address = Address.of(Objects.requireNonNull(request.address()), request.detailAddress());
    this.location =
        Location.of(
            Objects.requireNonNull(request.latitude()),
            Objects.requireNonNull(request.longitude()));
  }
}
