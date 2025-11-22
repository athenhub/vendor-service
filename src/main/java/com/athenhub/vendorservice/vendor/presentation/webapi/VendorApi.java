package com.athenhub.vendorservice.vendor.presentation.webapi;

import com.athenhub.commonmvc.security.AuthenticatedUser;
import com.athenhub.vendorservice.vendor.application.service.VendorFinder;
import com.athenhub.vendorservice.vendor.application.service.VendorManager;
import com.athenhub.vendorservice.vendor.application.service.VendorRegister;
import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.dto.VendorSearchCondition;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorRegisterRequest;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorUpdateRequest;
import com.athenhub.vendorservice.vendor.presentation.webapi.dto.VendorDeleteResponse;
import com.athenhub.vendorservice.vendor.presentation.webapi.dto.VendorFindResponse;
import com.athenhub.vendorservice.vendor.presentation.webapi.dto.VendorRegisterResponse;
import com.athenhub.vendorservice.vendor.presentation.webapi.dto.VendorUpdateResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 업체(Vendor) 관련 REST API 컨트롤러.
 *
 * <p>업체 등록, 조회, 정보 수정, 삭제 기능을 제공하며, 스프링 시큐리티의 역할 기반 접근 제어(@PreAuthorize)를 사용하여 요청 권한을 검증한다.
 *
 * <p>비즈니스 로직은 애플리케이션 계층의 {@link VendorRegister}, {@link VendorFinder}, {@link VendorManager} 인터페이스를
 * 통해 수행된다.
 *
 * <h2>제공 기능</h2>
 *
 * <ul>
 *   <li>POST /v1/vendors — 업체 등록
 *   <li>GET /v1/vendors/{vendorId} — 업체 단건 조회
 *   <li>PUT /v1/vendors/{vendorId} — 업체 정보 수정
 *   <li>DELETE /v1/vendors/{vendorId} — 업체 삭제
 * </ul>
 *
 * <p>각 요청은 인증된 사용자 정보({@link AuthenticatedUser})를 활용해 감사(Audit) 및 권한 확인에 사용된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class VendorApi {
  private final VendorRegister vendorRegister;
  private final VendorFinder vendorFinder;
  private final VendorManager vendorManager;

  /**
   * 업체를 신규 등록한다.
   *
   * <p>등록 권한은 MASTER_MANAGER, HUB_MANAGER 역할을 가진 사용자만 가능하다.
   *
   * @param requestUser 인증된 사용자 정보
   * @param registerRequest 업체 등록 요청 데이터
   * @return 등록된 업체 정보를 담은 {@link VendorRegisterResponse}
   */
  @PreAuthorize("hasAnyRole('MASTER_MANAGER', 'HUB_MANAGER')")
  @PostMapping("/v1/vendors")
  public VendorRegisterResponse register(
      @AuthenticationPrincipal AuthenticatedUser requestUser,
      @RequestBody VendorRegisterRequest registerRequest) {
    Vendor vendor = vendorRegister.register(registerRequest, requestUser.id());

    return VendorRegisterResponse.from(vendor);
  }

  /**
   * 특정 업체를 ID 기반으로 조회한다.
   *
   * <p>조회 권한은 MASTER_MANAGER, HUB_MANAGER, SHIPPING_AGENT, VENDOR_AGENT가 포함된다.
   *
   * @param vendorId 조회할 업체의 식별자(UUID)
   * @return 조회된 업체 정보를 담은 {@link VendorFindResponse}
   */
  @PreAuthorize("hasAnyRole('MASTER_MANAGER', 'HUB_MANAGER', 'SHIPPING_AGENT', 'VENDOR_AGENT')")
  @GetMapping("/v1/vendors/{vendorId}")
  public VendorFindResponse find(@PathVariable UUID vendorId) {
    Vendor vendor = vendorFinder.find(vendorId);

    return VendorFindResponse.from(vendor);
  }

  /**
   * 업체 검색 API.
   *
   * <p>해당 엔드포인트는 다양한 검색 조건을 기반으로 업체 목록을 조회한다. 검색 조건은 {@link VendorSearchCondition} 으로 전달되며, Spring
   * MVC의 {@link ModelAttribute} 바인딩을 통해 쿼리 파라미터에서 자동 매핑된다.
   *
   * <p>검색 결과는 {@link Pageable} 을 사용하여 페이징 처리되며, 결과는 {@link VendorFindResponse} 형태로 매핑된 페이지 객체로
   * 반환된다.
   *
   * <p>조회 권한은 MASTER_MANAGER, HUB_MANAGER, SHIPPING_AGENT, VENDOR_AGENT가 포함된다.
   *
   * @param searchCondition 업체 검색 조건. 쿼리스트링을 통해 전달된 파라미터가 자동으로 바인딩된다.
   * @param pageable 페이징 및 정렬 정보.
   * @return 검색 조건에 부합하는 업체 정보를 {@link VendorFindResponse} 형태로 반환하는 페이지 객체.
   */
  @PreAuthorize("hasAnyRole('MASTER_MANAGER', 'HUB_MANAGER', 'SHIPPING_AGENT', 'VENDOR_AGENT')")
  @GetMapping("/v1/vendors")
  public Page<VendorFindResponse> search(
      @ModelAttribute VendorSearchCondition searchCondition, Pageable pageable) {
    Page<Vendor> vendors = vendorFinder.search(searchCondition, pageable);

    return vendors.map(VendorFindResponse::from);
  }

  /**
   * 업체 정보를 수정한다.
   *
   * <p>수정 권한은 MASTER_MANAGER, HUB_MANAGER 역할만 허용된다.
   *
   * @param requestUser 인증된 사용자 정보
   * @param vendorId 수정 대상 업체 ID
   * @param updateRequest 수정 요청 데이터
   * @return 수정된 업체 정보를 담은 {@link VendorUpdateResponse}
   */
  @PreAuthorize("hasAnyRole('MASTER_MANAGER', 'HUB_MANAGER')")
  @PutMapping("/v1/vendors/{vendorId}")
  public VendorUpdateResponse updateInfo(
      @AuthenticationPrincipal AuthenticatedUser requestUser,
      @PathVariable UUID vendorId,
      @RequestBody VendorUpdateRequest updateRequest) {
    Vendor vendor = vendorManager.updateInfo(vendorId, updateRequest, requestUser.id());

    return VendorUpdateResponse.from(vendor);
  }

  /**
   * 업체를 삭제 처리한다.
   *
   * <p>삭제 권한은 MASTER_MANAGER, HUB_MANAGER 역할만 허용된다.
   *
   * @param requestUser 인증된 사용자 정보
   * @param vendorId 삭제 대상 업체 ID
   * @return 삭제 처리된 업체 정보를 담은 {@link VendorDeleteResponse}
   */
  @PreAuthorize("hasAnyRole('MASTER_MANAGER', 'HUB_MANAGER')")
  @DeleteMapping("/v1/vendors/{vendorId}")
  public VendorDeleteResponse delete(
      @AuthenticationPrincipal AuthenticatedUser requestUser, @PathVariable UUID vendorId) {
    Vendor vendor = vendorManager.delete(vendorId, requestUser.getUsername(), requestUser.id());

    return VendorDeleteResponse.from(vendor);
  }
}
