package com.athenhub.vendorservice.vendor.domain.vo;

/**
 * 업체 담당자(VendorAgent) 정보를 표현하는 읽기 전용 DTO(record).
 *
 * <p>업체에 속한 담당자의 핵심 정보를 보유하며, 조회 응답이나 내부 서비스 간 전달 객체로 사용된다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>{@code id} — 담당자 ID
 *   <li>{@code name} — 담당자 이름
 *   <li>{@code username} — 담당자 계정명
 *   <li>{@code slackId} — Slack 사용자 ID
 * </ul>
 *
 * <p>해당 객체는 불변(immutable) 구조로 제공되며, 기본적으로 조회 목적에 사용된다.
 *
 * @param id 업체 담당자 ID
 * @param name 담당자 이름
 * @param username 담당자 계정명
 * @param slackId Slack 사용자 ID
 * @author 김형섭
 * @since 1.0.0
 */
public record VendorAgent(VendorAgentId id, String name, String username, String slackId) {}
