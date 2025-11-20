package com.athenhub.vendorservice.vendor.domain.service;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.VendorRepository;
import com.athenhub.vendorservice.vendor.domain.vo.VendorId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 업체(Vendor) 조회 기능을 제공하는 서비스 구현체.
 *
 * <p>해당 서비스는 {@link VendorFinder} 인터페이스를 구현하며, 업체 조회와 관련된 읽기(Read) 작업만 담당한다. 조회 전용 서비스이므로 전체 트랜잭션은
 * {@code readOnly = true} 설정이 적용된다.
 *
 * <h2>역할</h2>
 *
 * <ul>
 *   <li>업체 단건 조회
 *   <li>조회 시 필요한 부가 검증 또는 예외 처리 수행
 * </ul>
 *
 * <p>조회 기능은 비즈니스 로직이 아닌 읽기 책임에 집중하도록 분리되어 있으며, 생성자 기반 의존성 주입은 {@link RequiredArgsConstructor}에 의해
 * 처리된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VendorQueryService implements VendorFinder {

  private final VendorRepository vendorRepository;

  @Override
  public Vendor find(VendorId vendorId) {
    return vendorRepository
        .findById(vendorId)
        .orElseThrow(
            () -> new IllegalArgumentException("업체 정보를 찾을수 없습니다. id: " + vendorId.toString()));
  }
}
