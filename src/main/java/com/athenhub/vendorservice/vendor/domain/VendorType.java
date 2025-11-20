package com.athenhub.vendorservice.vendor.domain;

/**
 * 업체(Vendor)의 유형을 나타내는 Enum.
 *
 * <h2>유형 정의</h2>
 *
 * <ul>
 *   <li>{@link #PRODUCER} — 생산업체(상품 또는 자원을 제공하는 측)
 *   <li>{@link #RECEIVER} — 수령업체(물품을 전달받는 측)
 * </ul>
 *
 * <p>비즈니스 로직에서 업체의 역할 구분 및 정책 처리에 사용된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public enum VendorType {
  PRODUCER,
  RECEIVER
}
