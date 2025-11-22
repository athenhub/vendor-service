package com.athenhub.vendorservice.vendor.infrastructure.client;

import com.athenhub.vendorservice.vendor.infrastructure.dto.MemberInfo;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Member 관련 외부 서비스 호출을 위한 Feign 클라이언트.
 *
 * <p>회원 서비스(member-service)와 통신하여 특정 멤버의 상세 정보를 조회한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@FeignClient("member-service")
public interface MemberServiceClient {
  /**
   * 지정된 멤버의 상세 정보를 조회한다.
   *
   * @param memberId 멤버 식별자(UUID)
   * @return 멤버 정보 객체 {@link MemberInfo}
   */
  @GetMapping("v1/members/{memberId}")
  MemberInfo getMemberInfo(@PathVariable("memberId") UUID memberId);
}
