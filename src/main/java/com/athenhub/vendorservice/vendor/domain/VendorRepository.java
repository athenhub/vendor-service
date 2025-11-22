package com.athenhub.vendorservice.vendor.domain;

import com.athenhub.vendorservice.vendor.domain.vo.HubId;
import com.athenhub.vendorservice.vendor.domain.vo.VendorId;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

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

  /**
   * 다양한 조건을 기반으로 {@link Vendor} 엔티티를 검색한다.
   *
   * <p>검색 조건은 모두 선택적(optional)이며, 입력되지 않은 조건은 필터링에 적용되지 않는다. 본 메서드는 다음과 같은 기준으로 검색을 수행한다:
   *
   * <ul>
   *   <li><b>VendorType(type)</b> — null이 아닌 경우, 해당 타입과 일치하는 업체만 조회한다.
   *   <li><b>HubId(hubId)</b> — null이 아닌 경우, 특정 허브에 소속된 업체만 조회한다.
   *   <li><b>keyword</b> — null이 아닌 경우, 이름(name), 주소(address.street), 상세주소(address.detail)에 부분 일치
   *       검색(LIKE %keyword%)을 수행한다.
   *   <li><b>includeDeleted</b> — true이면 삭제된 업체도 포함하여 조회하고, false이면 삭제되지 않은(v.deletedAt IS NULL)
   *       업체만 조회한다.
   * </ul>
   *
   * <p>페이징 처리는 Spring Data의 {@link Pageable}을 통해 적용되며, 반환 결과는 {@link Page} 형태로 제공된다.
   *
   * @param type 검색할 업체 타입 (null 허용 — 조건 미적용)
   * @param hubId 특정 허브 소속 업체 검색 조건 (null 허용 — 조건 미적용)
   * @param keyword 업체 이름 또는 주소 기반 부분 검색 키워드 (null 허용 — 조건 미적용)
   * @param includeDeleted 삭제된 업체를 포함해 검색할지 여부
   * @param pageable 페이징 및 정렬 정보
   * @return 검색 조건을 적용한 {@link Vendor} 페이지 결과
   */
  @Query(
      """
          SELECT v FROM Vendor v
          WHERE (:type IS NULL OR v.type = :type)
          AND (:hubId IS NULL OR v.hubId = :hubId)
          AND (
            :keyword IS NULL OR (
              upper(v.name) LIKE %:keyword%
              OR upper(v.address.street) LIKE %:keyword%
              OR upper(v.address.detail) LIKE %:keyword%
            )
          )
          AND (:includeDeleted = TRUE OR v.deletedAt IS NULL)
      """)
  Page<Vendor> search(
      @Param("type") VendorType type,
      @Param("hubId") HubId hubId,
      @Param("keyword") String keyword,
      @Param("includeDeleted") boolean includeDeleted,
      Pageable pageable);
}
