package com.athenhub.vendorservice.vendor.infrastructure.dto;

import com.athenhub.vendorservice.vendor.infrastructure.MemberRole;
import java.util.UUID;

/**
 * 허브 관리자 정보를 나타내는 DTO.
 *
 * <p>각 허브 관리자 개별 정보를 저장하며, {@link MemberRole}을 통해 역할을 명시한다.
 *
 * @param id 관리자 식별자(UUID)
 * @param name 관리자 이름
 * @param username 시스템 계정명
 * @param slackId Slack ID
 * @param role 관리자 역할 {@link MemberRole}
 * @author 김형섭
 * @since 1.0.0
 */
public record HubManager(UUID id, String name, String username, String slackId, MemberRole role) {}
