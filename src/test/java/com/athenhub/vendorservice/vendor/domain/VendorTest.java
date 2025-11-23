package com.athenhub.vendorservice.vendor.domain;

import static com.athenhub.vendorservice.vendor.VendorFixture.create;
import static com.athenhub.vendorservice.vendor.VendorFixture.createRegisterRequest;
import static com.athenhub.vendorservice.vendor.VendorFixture.createUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.athenhub.vendorservice.vendor.domain.dto.request.VendorRegisterRequest;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorUpdateRequest;
import com.athenhub.vendorservice.vendor.domain.exception.PermissionException;
import com.athenhub.vendorservice.vendor.domain.service.HubExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.MemberExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.PermissionChecker;
import com.athenhub.vendorservice.vendor.domain.vo.Address;
import com.athenhub.vendorservice.vendor.domain.vo.Coordinate;
import com.athenhub.vendorservice.vendor.domain.vo.HubId;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VendorTest {

  @Mock PermissionChecker permissionChecker;

  @Mock HubExistenceChecker hubExistenceChecker;

  @Mock MemberExistenceChecker memberExistenceChecker;

  Vendor vendor;

  private final UUID requestId = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    vendor = create(permissionChecker, hubExistenceChecker, memberExistenceChecker);
  }

  @Test
  void register() {
    when(permissionChecker.hasRegisterPermission(any(), any(HubId.class))).thenReturn(true);
    when(hubExistenceChecker.hasHub(any(HubId.class))).thenReturn(true);
    when(memberExistenceChecker.hasMember(any(UUID.class))).thenReturn(true);

    VendorRegisterRequest request = createRegisterRequest();
    Vendor vendor =
        Vendor.register(
            request, permissionChecker, hubExistenceChecker, memberExistenceChecker, requestId);

    assertThat(vendor.getId()).isNotNull();
    assertThat(vendor.getName()).isEqualTo(request.name());
    assertThat(vendor.getType()).isEqualTo(request.type());
    assertThat(vendor.getHubId()).isEqualTo(HubId.of(request.hubId()));
    assertThat(vendor.getAddress())
        .isEqualTo(Address.of(request.streetAddress(), request.detailAddress()));
    assertThat(vendor.getCoordinate())
        .isEqualTo(Coordinate.of(request.latitude(), request.longitude()));
  }

  @Test
  void registerHasNotPermission() {
    VendorRegisterRequest request = createRegisterRequest();

    when(permissionChecker.hasRegisterPermission(any(), any(HubId.class))).thenReturn(false);

    assertThatThrownBy(
            () ->
                Vendor.register(
                    request,
                    permissionChecker,
                    hubExistenceChecker,
                    memberExistenceChecker,
                    requestId))
        .isInstanceOf(PermissionException.class);
  }

  @Test
  void registerIfHubNotExists() {
    VendorRegisterRequest request = createRegisterRequest();

    when(permissionChecker.hasRegisterPermission(any(), any(HubId.class))).thenReturn(true);
    when(hubExistenceChecker.hasHub(any(HubId.class))).thenReturn(false);

    assertThatThrownBy(
            () ->
                Vendor.register(
                    request,
                    permissionChecker,
                    hubExistenceChecker,
                    memberExistenceChecker,
                    requestId))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void registerIfMemberNotExists() {
    VendorRegisterRequest request = createRegisterRequest();

    when(permissionChecker.hasRegisterPermission(any(), any(HubId.class))).thenReturn(true);
    when(hubExistenceChecker.hasHub(any(HubId.class))).thenReturn(true);
    when(memberExistenceChecker.hasMember(any(UUID.class))).thenReturn(false);

    assertThatThrownBy(
            () ->
                Vendor.register(
                    request,
                    permissionChecker,
                    hubExistenceChecker,
                    memberExistenceChecker,
                    requestId))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void updateInfo() {
    VendorUpdateRequest request = createUpdateRequest();

    when(permissionChecker.hasManagePermission(any(), any(HubId.class))).thenReturn(true);
    when(hubExistenceChecker.hasHub(any(HubId.class))).thenReturn(true);

    vendor.updateInfo(request, permissionChecker, hubExistenceChecker, requestId);

    assertThat(vendor.getName()).isEqualTo(request.name());
    assertThat(vendor.getType()).isEqualTo(request.type());
    assertThat(vendor.getHubId()).isEqualTo(HubId.of(request.hubId()));
    assertThat(vendor.getAddress())
        .isEqualTo(Address.of(request.streetAddress(), request.detailAddress()));
    assertThat(vendor.getCoordinate())
        .isEqualTo(Coordinate.of(request.latitude(), request.longitude()));
  }

  @Test
  void updateInfoHasNotPermission() {
    VendorUpdateRequest request = createUpdateRequest();

    when(permissionChecker.hasManagePermission(any(), any(HubId.class))).thenReturn(false);

    assertThatThrownBy(
            () -> vendor.updateInfo(request, permissionChecker, hubExistenceChecker, requestId))
        .isInstanceOf(PermissionException.class);
  }

  @Test
  void updateInfoIfHubNotExists() {
    VendorUpdateRequest request = createUpdateRequest();

    when(permissionChecker.hasManagePermission(any(), any(HubId.class))).thenReturn(true);
    when(hubExistenceChecker.hasHub(any(HubId.class))).thenReturn(false);

    assertThatThrownBy(
            () -> vendor.updateInfo(request, permissionChecker, hubExistenceChecker, requestId))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void delete() {
    when(permissionChecker.hasManagePermission(any(), any(HubId.class))).thenReturn(true);

    vendor.delete("test", permissionChecker, requestId);

    assertThat(vendor.getDeletedBy()).isEqualTo("test");
    assertThat(vendor.getDeletedAt()).isNotNull();
  }

  @Test
  void deleteHasNotPermission() {
    when(permissionChecker.hasManagePermission(any(), any(HubId.class))).thenReturn(false);

    assertThatThrownBy(() -> vendor.delete("test", permissionChecker, requestId))
        .isInstanceOf(PermissionException.class);
  }
}
