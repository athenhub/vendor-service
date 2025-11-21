package com.athenhub.vendorservice.vendor.presentation.webapi;

import com.athenhub.commonmvc.security.AuthenticatedUser;
import com.athenhub.vendorservice.vendor.application.service.VendorFinder;
import com.athenhub.vendorservice.vendor.application.service.VendorManager;
import com.athenhub.vendorservice.vendor.application.service.VendorRegister;
import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorRegisterRequest;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorUpdateRequest;
import com.athenhub.vendorservice.vendor.presentation.webapi.dto.VendorDeleteResponse;
import com.athenhub.vendorservice.vendor.presentation.webapi.dto.VendorFindResponse;
import com.athenhub.vendorservice.vendor.presentation.webapi.dto.VendorRegisterResponse;
import com.athenhub.vendorservice.vendor.presentation.webapi.dto.VendorUpdateResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
