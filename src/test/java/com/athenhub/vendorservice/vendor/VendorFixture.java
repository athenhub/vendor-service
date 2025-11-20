package com.athenhub.vendorservice.vendor;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.VendorType;
import com.athenhub.vendorservice.vendor.domain.vo.request.VendorRegisterRequest;
import com.athenhub.vendorservice.vendor.domain.vo.request.VendorUpdateRequest;
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
 *   <li>{@link #create()} – 기본 Vendor 엔티티 생성
 *   <li>{@link #create(VendorRegisterRequest)} – 지정된 요청 기반 Vendor 생성
 *   <li>{@link #createUpdateRequest()} – Vendor 수정 요청 DTO 생성
 * </ul>
 *
 * <p>이 클래스는 테스트 전용이며, 프로덕션 코드에서는 사용되지 않는다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public class VendorFixture {

  /**
   * 기본 Vendor 등록 요청 DTO를 생성한다.
   *
   * @return 미리 정의된 값으로 구성된 {@link VendorRegisterRequest}
   */
  public static VendorRegisterRequest createRegisterRequest() {
    return new VendorRegisterRequest(
        "스파르타 테크",
        UUID.randomUUID(),
        VendorType.PRODUCER,
        "서울특별시 강남구 도곡로 112",
        "4층 TECH3",
        37.489662,
        127.032855);
  }

  /**
   * 기본 Vendor 엔티티를 생성한다.
   *
   * <p>내부적으로 {@link #createRegisterRequest()} 를 사용하여 Vendor 등록 요청을 생성한 뒤, 도메인 엔티티 생성 메서드인 {@link
   * Vendor#register(VendorRegisterRequest)} 를 호출한다.
   *
   * @return 생성된 {@link Vendor}
   */
  public static Vendor create() {
    return create(createRegisterRequest());
  }

  /**
   * 지정된 등록 요청 값을 기반으로 Vendor 엔티티를 생성한다.
   *
   * @param request Vendor 등록 요청 DTO
   * @return 생성된 {@link Vendor}
   */
  public static Vendor create(VendorRegisterRequest request) {
    return Vendor.register(request);
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
