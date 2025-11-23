package com.athenhub.vendorservice.vendor.presentation.webapi.dto;

import java.util.UUID;

/**
 * 업체 담당자 변경 요청 정보를 담는 DTO.
 *
 * <p>해당 요청 객체는 특정 업체(Vendor)의 담당자를 다른 담당자로 교체할 때 사용되며, 교체될 새로운 담당자의 식별자(UUID)를 포함한다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>{@code newAgentId} — 새로 지정할 업체 담당자의 식별자
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record VendorAgentChangeRequest(UUID newAgentId) {}
