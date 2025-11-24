package com.athenhub.vendorservice.vendor.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.athenhub.vendorservice.vendor.domain.vo.HubId;
import com.athenhub.vendorservice.vendor.domain.vo.VendorAgentId;
import com.athenhub.vendorservice.vendor.infrastructure.client.HubServiceClient;
import com.athenhub.vendorservice.vendor.infrastructure.client.MemberServiceClient;
import com.athenhub.vendorservice.vendor.infrastructure.dto.HubManager;
import com.athenhub.vendorservice.vendor.infrastructure.dto.MemberInfo;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PermissionCheckServiceTest {

  @Mock MemberServiceClient memberServiceClient;

  @Mock HubServiceClient hubServiceClient;

  @InjectMocks PermissionCheckService permissionChecker;

  @Test
  void registerPermissionIfMasterManagerReturnTrue() {
    MemberInfo memberInfo = createMemberInfo(UUID.randomUUID(), MemberRole.MASTER_MANAGER, MemberStatus.ACTIVATED, null, true);

    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    assertThat(
            permissionChecker.hasRegisterPermission(memberInfo.id(), HubId.of(UUID.randomUUID())))
        .isTrue();
  }

  @Test
  void registerPermissionIfManagerOfHubReturnTrue() {
    UUID memberId = UUID.randomUUID();
    MemberInfo memberInfo = createMemberInfo(memberId, MemberRole.HUB_MANAGER, MemberStatus.ACTIVATED, null, true);
    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    UUID hubId = UUID.randomUUID();
    HubManager hubManager = new HubManager(memberId, "테스트 회원", "testMember", "testSlackId");
    when(hubServiceClient.getHubManager(hubId)).thenReturn(hubManager);

    assertThat(permissionChecker.hasRegisterPermission(memberInfo.id(), HubId.of(hubId))).isTrue();
  }

  @Test
  void registerPermissionIfNotManagerOfHubReturnFalse() {
    UUID memberId = UUID.randomUUID();
    MemberInfo memberInfo = createMemberInfo(memberId, MemberRole.HUB_MANAGER, MemberStatus.ACTIVATED, null, true);
    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    UUID hubId = UUID.randomUUID();
    HubManager hubManager =
        new HubManager(UUID.randomUUID(), "테스트 회원", "testMember", "testSlackId");
    when(hubServiceClient.getHubManager(hubId)).thenReturn(hubManager);

    assertThat(permissionChecker.hasRegisterPermission(memberInfo.id(), HubId.of(hubId))).isFalse();
  }

  @Test
  void registerPermissionIfNotManagerReturnFalse() {
    MemberInfo memberInfo = createMemberInfo(UUID.randomUUID(), MemberRole.SHIPPING_AGENT, MemberStatus.ACTIVATED, null, true);

    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    assertThat(
            permissionChecker.hasRegisterPermission(memberInfo.id(), HubId.of(UUID.randomUUID())))
        .isFalse();
  }

  @Test
  void registerPermissionIfDeactivatedReturnFalse() {
    MemberInfo memberInfo = createMemberInfo(UUID.randomUUID(), MemberRole.MASTER_MANAGER, MemberStatus.DEACTIVATED, null, false);

    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    assertThat(
        permissionChecker.hasRegisterPermission(memberInfo.id(), HubId.of(UUID.randomUUID())))
        .isFalse();
  }

  @Test
  void updatePermissionIfVendorAgentReturnTrue() {
    MemberInfo memberInfo = createMemberInfo(UUID.randomUUID(), MemberRole.VENDOR_AGENT, MemberStatus.ACTIVATED, null, true);

    assertThat(
            permissionChecker.hasUpdatePermission(
                memberInfo.id(), HubId.of(UUID.randomUUID()), VendorAgentId.of(memberInfo.id())))
        .isTrue();
  }

  @Test
  void updatePermissionIfMasterManagerReturnTrue() {
    MemberInfo memberInfo = createMemberInfo(UUID.randomUUID(), MemberRole.MASTER_MANAGER, MemberStatus.ACTIVATED, null, true);

    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    assertThat(
            permissionChecker.hasUpdatePermission(
                memberInfo.id(), HubId.of(UUID.randomUUID()), VendorAgentId.of(UUID.randomUUID())))
        .isTrue();
  }

  @Test
  void updatePermissionIfManagerOfHubReturnTrue() {
    UUID memberId = UUID.randomUUID();
    MemberInfo memberInfo = createMemberInfo(memberId, MemberRole.HUB_MANAGER, MemberStatus.ACTIVATED, null, true);
    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    UUID hubId = UUID.randomUUID();
    HubManager hubManager = new HubManager(memberId, "테스트 회원", "testMember", "testSlackId");
    when(hubServiceClient.getHubManager(hubId)).thenReturn(hubManager);

    assertThat(
            permissionChecker.hasUpdatePermission(
                memberInfo.id(), HubId.of(hubId), VendorAgentId.of(UUID.randomUUID())))
        .isTrue();
  }

  @Test
  void updatePermissionIfNotManagerOfHubAndVendorAgentReturnFalse() {
    UUID memberId = UUID.randomUUID();
    MemberInfo memberInfo = createMemberInfo(memberId, MemberRole.HUB_MANAGER, MemberStatus.ACTIVATED, null, true);
    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    UUID hubId = UUID.randomUUID();
    HubManager hubManager =
        new HubManager(UUID.randomUUID(), "테스트 회원", "testMember", "testSlackId");
    when(hubServiceClient.getHubManager(hubId)).thenReturn(hubManager);

    assertThat(
            permissionChecker.hasUpdatePermission(
                memberInfo.id(), HubId.of(hubId), VendorAgentId.of(UUID.randomUUID())))
        .isFalse();
  }

  @Test
  void updatePermissionIfNotManagerAndVendorAgentReturnFalse() {
    MemberInfo memberInfo = createMemberInfo(UUID.randomUUID(), MemberRole.SHIPPING_AGENT, MemberStatus.ACTIVATED, null, true);

    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    assertThat(
            permissionChecker.hasUpdatePermission(
                memberInfo.id(), HubId.of(UUID.randomUUID()), VendorAgentId.of(UUID.randomUUID())))
        .isFalse();
  }

  @Test
  void updatePermissionIfDeactivatedReturnFalse() {
    MemberInfo memberInfo = createMemberInfo(UUID.randomUUID(), MemberRole.MASTER_MANAGER, MemberStatus.DEACTIVATED, null, false);

    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    assertThat(
        permissionChecker.hasUpdatePermission(
            memberInfo.id(), HubId.of(UUID.randomUUID()), VendorAgentId.of(UUID.randomUUID())))
        .isFalse();
  }

  @Test
  void deletePermissionIfMasterManagerReturnTrue() {
    MemberInfo memberInfo = createMemberInfo(UUID.randomUUID(), MemberRole.MASTER_MANAGER, MemberStatus.ACTIVATED, null, true);

    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    assertThat(permissionChecker.hasDeletePermission(memberInfo.id(), HubId.of(UUID.randomUUID())))
        .isTrue();
  }

  @Test
  void deletePermissionIfManagerOfHubReturnTrue() {
    UUID memberId = UUID.randomUUID();
    MemberInfo memberInfo = createMemberInfo(memberId, MemberRole.HUB_MANAGER, MemberStatus.ACTIVATED, null, true);
    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    UUID hubId = UUID.randomUUID();
    HubManager hubManager = new HubManager(memberId, "테스트 회원", "testMember", "testSlackId");
    when(hubServiceClient.getHubManager(hubId)).thenReturn(hubManager);

    assertThat(permissionChecker.hasDeletePermission(memberInfo.id(), HubId.of(hubId))).isTrue();
  }

  @Test
  void deletePermissionIfNotManagerOfHubReturnFalse() {
    UUID memberId = UUID.randomUUID();
    MemberInfo memberInfo = createMemberInfo(memberId, MemberRole.HUB_MANAGER, MemberStatus.ACTIVATED, null, true);
    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    UUID hubId = UUID.randomUUID();
    HubManager hubManager =
        new HubManager(UUID.randomUUID(), "테스트 회원", "testMember", "testSlackId");
    when(hubServiceClient.getHubManager(hubId)).thenReturn(hubManager);

    assertThat(permissionChecker.hasDeletePermission(memberInfo.id(), HubId.of(hubId))).isFalse();
  }

  @Test
  void deletePermissionIfNotManagerReturnFalse() {
    MemberInfo memberInfo = createMemberInfo(UUID.randomUUID(), MemberRole.SHIPPING_AGENT, MemberStatus.ACTIVATED, null, true);

    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    assertThat(permissionChecker.hasDeletePermission(memberInfo.id(), HubId.of(UUID.randomUUID())))
        .isFalse();
  }

  @Test
  void deletePermissionIfDeactivatedReturnFalse() {
    MemberInfo memberInfo = createMemberInfo(UUID.randomUUID(), MemberRole.MASTER_MANAGER, MemberStatus.DEACTIVATED, null, false);

    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    assertThat(permissionChecker.hasDeletePermission(memberInfo.id(), HubId.of(UUID.randomUUID())))
        .isFalse();
  }

  private static MemberInfo createMemberInfo(UUID memberId, MemberRole role, MemberStatus status, LocalDateTime deletedAt, boolean isActivated) {
    return new MemberInfo(
        memberId,
        "테스트 회원",
        "testMember",
        "testSlackId",
        "서울 물류",
        role,
        status,
        LocalDateTime.now(),
        LocalDateTime.now(),
        deletedAt,
        null,
        isActivated
        );
  }
}
