package com.athenhub.vendorservice.vendor.application.service;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.VendorRepository;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorRegisterRequest;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorUpdateRequest;
import com.athenhub.vendorservice.vendor.domain.service.HubExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.MemberExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.PermissionChecker;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * 업체(Vendor) 등록 및 관리 기능을 제공하는 서비스 구현체.
 *
 * <p>해당 서비스는 {@link VendorRegister} 및 {@link VendorManager} 인터페이스를 구현하여 업체 등록, 정보 수정, 삭제 등
 * 쓰기(Write) 작업을 처리한다. 트랜잭션이 필요한 도메인 변경 작업이므로 클래스 전체에 {@code @Transactional}이 적용된다.
 *
 * <h2>역할</h2>
 *
 * <ul>
 *   <li>신규 업체 등록
 *   <li>기존 업체 정보 수정
 *   <li>업체 삭제 처리
 * </ul>
 *
 * <p>{@link Validated} 애너테이션을 통해 메서드 파라미터에 대한 Bean Validation 검증이 수행되며, 서비스 계층에서도 유효성 검사 규칙을 강제한다.
 *
 * <p>생성자 주입은 {@link RequiredArgsConstructor}에 의해 자동 생성된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class VendorManageService implements VendorRegister, VendorManager {

  private final VendorRepository vendorRepository;
  private final VendorFinder vendorFinder;
  private final PermissionChecker permissionChecker;
  private final HubExistenceChecker hubExistenceChecker;
  private final MemberExistenceChecker memberExistenceChecker;

  @Override
  public Vendor register(VendorRegisterRequest registerRequest, UUID requestId) {
    Vendor vendor =
        Vendor.register(
            registerRequest,
            permissionChecker,
            hubExistenceChecker,
            memberExistenceChecker,
            requestId);

    return vendorRepository.save(vendor);
  }

  @Override
  public Vendor updateInfo(UUID vendorId, VendorUpdateRequest updateRequest, UUID requestId) {
    Vendor vendor = vendorFinder.find(vendorId);

    vendor.updateInfo(updateRequest, permissionChecker, hubExistenceChecker, requestId);

    return vendorRepository.save(vendor);
  }

  @Override
  public Vendor delete(UUID vendorId, String deleteBy, UUID requestId) {
    Vendor vendor = vendorFinder.find(vendorId);

    vendor.delete(deleteBy, permissionChecker, requestId);

    return vendorRepository.save(vendor);
  }
}
