package com.athenhub.vendorservice.vendor.application.service;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * 업체(Vendor)에 대한 정보 변경 및 삭제를 담당하는 도메인 서비스 인터페이스.
 *
 * <p>업체 정보 수정, 삭제 등 상태 변경 작업을 수행하며, 각 작업은 도메인 규칙 및 유효성 검증을 기반으로 처리된다.
 *
 * <h2>역할</h2>
 *
 * <ul>
 *   <li>업체 정보 수정(주소, 타입, 명칭 등)
 *   <li>업체 삭제 및 삭제자 정보 기록
 *   <li>식별자 및 요청 데이터에 대한 유효성 검사
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface VendorManager {
  /**
   * 업체 정보를 수정한다.
   *
   * @param vendorId 수정할 업체 ID (필수)
   * @param updateRequest 수정 요청 DTO (필수, 유효성 검사 적용)
   * @param requestId 변경 요청자 (필수)
   * @param requestUsername 요청자 계정 (필수)
   * @return 수정된 {@link Vendor} 엔티티
   */
  Vendor updateInfo(
      @NotNull UUID vendorId,
      @Valid VendorUpdateRequest updateRequest,
      @NotNull UUID requestId,
      @NotNull String requestUsername);

  /**
   * 업체를 삭제한다.
   *
   * @param vendorId 삭제할 업체 ID (필수)
   * @param deleteBy 삭제 요청자 (필수)
   * @param requestId 변경 요청자 (필수)
   * @param requestUsername 요청자 계정 (필수)
   * @return 삭제 처리된 {@link Vendor} 엔티티
   */
  Vendor delete(
      @NotNull UUID vendorId,
      @NotBlank String deleteBy,
      @NotNull UUID requestId,
      @NotNull String requestUsername);

  /**
   * 업체 담당자를 변경한다.
   *
   * @param vendorId 변경할 업체 ID (필수)
   * @param newAgentId 변경할 업체 담당자 ID (필수)
   * @param requestId 변경 요청자 (필수)
   * @param requestUsername 요청자 계정 (필수)
   */
  void changeAgent(
      @NotNull UUID vendorId,
      @NotNull UUID newAgentId,
      @NotNull UUID requestId,
      @NotNull String requestUsername);
}
