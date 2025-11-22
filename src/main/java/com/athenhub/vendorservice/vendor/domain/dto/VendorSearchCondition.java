package com.athenhub.vendorservice.vendor.domain.dto;

import com.athenhub.vendorservice.vendor.domain.VendorType;
import com.athenhub.vendorservice.vendor.domain.vo.HubId;

/**
 * 업체 검색을 위한 다양한 조건을 담는 불변(immutable) 검색 조건 객체.
 *
 * <p>해당 레코드는 업체 검색 시 적용할 필터링 조건을 하나의 객체로 묶어 전달하기 위한 목적을 가진다. 모든 필드는 선택적(optional)이므로, null 또는 기본값이
 * 들어올 경우 해당 조건은 검색에 적용되지 않는다.
 *
 * <h2>검색 조건</h2>
 *
 * <ul>
 *   <li><b>type</b> — 업체 유형 필터링 조건 (null 시 조건 미적용)
 *   <li><b>hubId</b> — 특정 허브에 소속된 업체 조회 조건 (null 시 조건 미적용)
 *   <li><b>keyword</b> — 이름, 주소, 상세주소 기반 부분 검색 키워드 (null 시 조건 미적용)
 *   <li><b>includeDeleted</b> — 삭제된 업체 포함 여부
 *   <li><b>pageable</b> — 페이징 및 정렬 정보
 * </ul>
 *
 * <p>해당 레코드는 도메인 서비스, 조회 서비스, 또는 Repository 계층에서 검색 조건 전달용으로 사용된다.
 *
 * @param type 검색할 업체 타입
 * @param hubId 검색할 허브 식별자
 * @param keyword 부분 일치 검색에 사용할 키워드
 * @param includeDeleted true일 경우 삭제된 업체도 결과에 포함
 * @author 김형섭
 * @since 1.0.0
 */
public record VendorSearchCondition(
    VendorType type, HubId hubId, String keyword, boolean includeDeleted) {

  /**
   * {@link VendorSearchCondition} 생성용 정적 팩토리 메서드.
   *
   * @param type 업체 타입 조건
   * @param hubId 허브 식별자 조건
   * @param keyword 검색 키워드
   * @param includeDeleted 삭제된 업체 포함 여부
   * @return 새로운 {@link VendorSearchCondition} 인스턴스
   */
  public static VendorSearchCondition of(
      VendorType type, HubId hubId, String keyword, boolean includeDeleted) {
    return new VendorSearchCondition(type, hubId, keyword, includeDeleted);
  }
}
