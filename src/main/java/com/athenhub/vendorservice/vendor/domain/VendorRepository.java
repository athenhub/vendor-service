package com.athenhub.vendorservice.vendor.domain;

import com.athenhub.vendorservice.vendor.domain.vo.VendorId;
import java.util.Optional;
import org.springframework.data.repository.Repository;

/**
 * 업체(Vendor) 엔티티에 대한 저장소 인터페이스.
 *
 * <p>Spring Data Repository 기반의 인터페이스로, 업체 엔티티의 저장 및 조회 기능을 제공한다. 해당 저장소는 애그리거트 루트인 {@link Vendor}를
 * 관리하며, 식별자로 {@link VendorId}를 사용한다.
 *
 * <h2>역할</h2>
 *
 * <ul>
 *   <li>Vendor 엔티티의 저장(Persist / Update)
 *   <li>식별자를 기반으로 한 단건 조회
 * </ul>
 *
 * <p>도메인 계층에서는 해당 저장소를 통해 영속성 레이어에 접근하며, Spring Data 구현체는 런타임 시 자동 생성된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface VendorRepository extends Repository<Vendor, VendorId> {
  /**
   * 업체 엔티티를 저장한다.
   *
   * <p>신규 엔티티는 생성되고, 기존 엔티티는 변경 사항이 반영된다.
   *
   * @param vendor 저장할 {@link Vendor} 엔티티
   * @return 저장된 {@link Vendor} 엔티티
   */
  Vendor save(Vendor vendor);

  /**
   * 식별자를 기준으로 업체를 조회한다.
   *
   * @param id 업체 식별자
   * @return 조회된 {@link Vendor} 엔티티(Optional)
   */
  Optional<Vendor> findById(VendorId id);
}
