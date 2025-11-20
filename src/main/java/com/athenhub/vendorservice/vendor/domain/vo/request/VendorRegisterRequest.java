package com.athenhub.vendorservice.vendor.domain.vo.request;

import com.athenhub.vendorservice.vendor.domain.VendorType;
import java.util.UUID;

/**
 * 업체 등록을 위한 요청 정보 DTO.
 *
 * <p>클라이언트가 Vendor를 신규 등록할 때 필요한 필드를 전달하며, 도메인 엔티티 생성 시 필수 값 검증 및 매핑에 사용된다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>name — 업체명
 *   <li>hubId — 소속 허브 식별자
 *   <li>type — 업체 유형(생산자/수령자)
 *   <li>address, detailAddress — 주소 정보
 *   <li>latitude, longitude — 위치 정보
 * </ul>
 *
 * <p>DTO 자체는 불변이며, 검증은 도메인 또는 서비스 레이어에서 처리한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record VendorRegisterRequest(
    String name,
    UUID hubId,
    VendorType type,
    String address,
    String detailAddress,
    Double latitude,
    Double longitude) {}
