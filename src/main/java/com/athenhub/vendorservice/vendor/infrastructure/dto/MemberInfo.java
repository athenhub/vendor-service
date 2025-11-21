package com.athenhub.vendorservice.vendor.infrastructure.dto;

import com.athenhub.vendorservice.vendor.infrastructure.MemberRole;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 회원(Member) 정보를 나타내는 DTO.
 *
 * <p>외부 회원 서비스(member-service)로부터 조회되는 회원 상세 정보를 담는다.
 *
 * @param id 회원 식별자(UUID)
 * @param name 회원 이름
 * @param username 시스템 계정명
 * @param slackId Slack ID
 * @param organizationName 소속 조직명
 * @param role 회원 역할 {@link MemberRole}
 * @param status 계정 상태 (예: ACTIVE, INACTIVE)
 * @param createdAt 계정 생성 일시
 * @param updatedAt 계정 수정 일시
 * @param deletedAt 계정 삭제 일시 (삭제되지 않았으면 {@code null})
 * @param deletedBy 계정 삭제 수행자 (삭제되지 않았으면 {@code null})
 * @author 김형섭
 * @since 1.0.0
 */
public record MemberInfo(
    UUID id,
    String name,
    String username,
    String slackId,
    String organizationName,
    MemberRole role,
    String status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    String deletedBy) {}
