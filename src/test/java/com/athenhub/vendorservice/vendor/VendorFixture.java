package com.athenhub.vendorservice.vendor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.VendorType;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorRegisterRequest;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorUpdateRequest;
import com.athenhub.vendorservice.vendor.domain.service.HubExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.MemberExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.PermissionChecker;
import com.athenhub.vendorservice.vendor.domain.vo.HubId;
import java.util.UUID;

/**
 * Vendor 관련 테스트 객체 생성을 지원하는 유틸리티 클래스.
 *
 * <p>Vendor 도메인을 테스트할 때 반복적으로 필요한 {@link Vendor}, {@link VendorRegisterRequest}, {@link
 * VendorUpdateRequest} 등을 손쉽게 생성할 수 있도록 정형화된 데이터를 제공한다. 랜덤 UUID 및 기본값을 포함하여 테스트 시 안정적인 픽스처를 구성하는 데
 * 활용된다.
 *
 * <h2>제공 기능</h2>
 *
 * <ul>
 *   <li>{@link #createRegisterRequest()} – Vendor 등록 요청 DTO 생성
 *   <li>{@link #createRegisterRequest(String, UUID, VendorType, String, String, UUID)} – 지정된 요청 기반
 *   <li>{@link #create(PermissionChecker, HubExistenceChecker, MemberExistenceChecker)} – 기본 Vendor 엔티티 생성
 *   <li>{@link #create(VendorRegisterRequest, PermissionChecker, HubExistenceChecker, MemberExistenceChecker)} – 지정된 요청 기반
 *       Vendor 생성
 *   <li>{@link #createUpdateRequest()} – Vendor 수정 요청 DTO 생성
 * </ul>
 *
 * <p>이 클래스는 테스트 전용이며, 프로덕션 코드에서는 사용되지 않는다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public class VendorFixture {

  private static final UUID requestId = UUID.randomUUID();

  /**
   * 기본 Vendor 등록 요청 DTO를 생성한다.
   *
   * @return 미리 정의된 값으로 구성된 {@link VendorRegisterRequest}
   */
  public static VendorRegisterRequest createRegisterRequest() {
    return createRegisterRequest(
        "스파르타 테크",
        UUID.randomUUID(),
        VendorType.PRODUCER,
        "서울특별시 강남구 도곡로 112",
        "4층 TECH3",
        UUID.randomUUID());
  }

  /**
   * 지정된 값으로 Vendor 등록 요청 DTO를 생성한다.
   *
   * <p>테스트 시 특정 값으로 Vendor 등록 요청을 만들고자 할 때 사용한다.
   *
   * @param name 등록할 업체명
   * @param hubId 소속 허브 식별자
   * @param type 벤더 유형
   * @param streetAddress 도로명 주소
   * @param detailAddress 상세 주소
   * @return 지정된 값으로 구성된 {@link VendorRegisterRequest}
   */
  public static VendorRegisterRequest createRegisterRequest(
      String name,
      UUID hubId,
      VendorType type,
      String streetAddress,
      String detailAddress,
      UUID agentId) {
    return new VendorRegisterRequest(
        name, hubId, type, streetAddress, detailAddress, 37.489662, 127.032855, agentId);
  }

  /**
   * 기본 Vendor 엔티티를 생성한다.
   *
   * <p>내부적으로 {@link #createRegisterRequest()} 를 사용하여 Vendor 등록 요청을 생성한 뒤, 도메인 엔티티 생성 메서드인 {@link
   * Vendor#register(VendorRegisterRequest, PermissionChecker, HubExistenceChecker,
   * MemberExistenceChecker, UUID)} 를 호출한다.
   *
   * @param permissionChecker 권한 검증 인터페이스
   * @param hubExistenceChecker 허브 존재 여부 검증 인터페이스
   * @param memberExistenceChecker 회원 존재 여부 검증 인터페이스
   * @return 생성된 {@link Vendor}
   */
  public static Vendor create(
      PermissionChecker permissionChecker,
      HubExistenceChecker hubExistenceChecker,
      MemberExistenceChecker memberExistenceChecker) {
    return create(
        createRegisterRequest(), permissionChecker, hubExistenceChecker, memberExistenceChecker);
  }

  /**
   * 지정된 등록 요청 값을 기반으로 Vendor 엔티티를 생성한다.
   *
   * @param request Vendor 등록 요청 DTO
   * @param permissionChecker 권한 검증 인터페이스
   * @param hubExistenceChecker 허브 존재 여부 검증 인터페이스
   * @return 생성된 {@link Vendor}
   */
  public static Vendor create(
      VendorRegisterRequest request,
      PermissionChecker permissionChecker,
      HubExistenceChecker hubExistenceChecker,
      MemberExistenceChecker memberExistenceChecker) {
    when(permissionChecker.hasRegisterPermission(any(), any(HubId.class))).thenReturn(true);
    when(hubExistenceChecker.hasHub(any(HubId.class))).thenReturn(true);
    when(memberExistenceChecker.hasMember(any(UUID.class))).thenReturn(true);

    return Vendor.register(
        request, permissionChecker, hubExistenceChecker, memberExistenceChecker, requestId);
  }

  /**
   * Vendor 수정 요청 DTO를 생성한다.
   *
   * @return Vendor 정보 변경을 위한 {@link VendorUpdateRequest}
   */
  public static VendorUpdateRequest createUpdateRequest() {
    return new VendorUpdateRequest(
        "아테네 테크",
        UUID.randomUUID(),
        VendorType.RECEIVER,
        "서울특별시 강남구 도곡로 113",
        "",
        37.490205,
        127.032838);
  }
}
