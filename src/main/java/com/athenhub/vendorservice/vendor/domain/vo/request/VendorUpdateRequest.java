package com.athenhub.vendorservice.vendor.domain.vo.request;

import com.athenhub.vendorservice.vendor.domain.VendorType;
import java.util.UUID;

/**
 * 업체 정보 수정을 위한 요청 DTO.
 *
 * <p>기존 Vendor의 정보 변경 시 전달되는 데이터 구조로, 등록 요청 DTO와 동일한 구조를 가지되, 도메인 엔티티의 변경 메서드에서 이용된다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>name — 업체명
 *   <li>hubId — 소속 허브 식별자
 *   <li>type — 업체 유형
 *   <li>address, detailAddress — 주소 정보
 *   <li>latitude, longitude — 위치 정보
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record VendorUpdateRequest(
    String name,
    UUID hubId,
    VendorType type,
    String address,
    String detailAddress,
    Double latitude,
    Double longitude) {}
