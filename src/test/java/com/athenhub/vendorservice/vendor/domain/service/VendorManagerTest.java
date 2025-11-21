package com.athenhub.vendorservice.vendor.domain.service;

import static com.athenhub.vendorservice.vendor.VendorFixture.createRegisterRequest;
import static com.athenhub.vendorservice.vendor.VendorFixture.createUpdateRequest;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.athenhub.vendorservice.vendor.application.service.VendorFinder;
import com.athenhub.vendorservice.vendor.application.service.VendorManager;
import com.athenhub.vendorservice.vendor.application.service.VendorRegister;
import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorUpdateRequest;
import com.athenhub.vendorservice.vendor.domain.exception.PermissionException;
import com.athenhub.vendorservice.vendor.domain.vo.Address;
import com.athenhub.vendorservice.vendor.domain.vo.Coordinate;
import com.athenhub.vendorservice.vendor.domain.vo.HubId;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class VendorManagerTest {

  @Autowired private VendorRegister vendorRegister;

  @Autowired private VendorFinder vendorFinder;

  @Autowired private VendorManager vendorManager;

  @Autowired private EntityManager entityManager;

  @MockitoBean private PermissionChecker permissionChecker;

  @MockitoBean private HubExistenceChecker hubExistenceChecker;

  private final UUID requestId = randomUUID();

  @Test
  void updateInfoInfo() {
    Vendor vendor = registerVendor();
    VendorUpdateRequest request = createUpdateRequest();

    when(permissionChecker.hasManagePermission(any(), any(HubId.class))).thenReturn(true);
    when(hubExistenceChecker.hasHub(any(HubId.class))).thenReturn(true);

    vendorManager.updateInfo(vendor.getId().toUuid(), request, requestId);
    entityManager.flush();
    entityManager.clear();

    vendor = vendorFinder.find(vendor.getId().toUuid());

    assertThat(vendor.getName()).isEqualTo(request.name());
    assertThat(vendor.getType()).isEqualTo(request.type());
    assertThat(vendor.getHubId()).isEqualTo(HubId.of(request.hubId()));
    assertThat(vendor.getAddress())
        .isEqualTo(Address.of(request.address(), request.detailAddress()));
    assertThat(vendor.getCoordinate())
        .isEqualTo(Coordinate.of(request.latitude(), request.longitude()));
  }

  @Test
  void updateInfoInfoHasNotPermission() {
    Vendor vendor = registerVendor();
    VendorUpdateRequest request = createUpdateRequest();

    when(permissionChecker.hasManagePermission(any(), any(HubId.class))).thenReturn(false);

    assertThatThrownBy(() -> vendorManager.updateInfo(vendor.getId().toUuid(), request, requestId))
        .isInstanceOf(PermissionException.class);
  }

  @Test
  void updateInfoInfoIfHubNotExists() {
    Vendor vendor = registerVendor();
    VendorUpdateRequest request = createUpdateRequest();

    when(permissionChecker.hasManagePermission(any(), any(HubId.class))).thenReturn(true);
    when(hubExistenceChecker.hasHub(any(HubId.class))).thenReturn(false);

    assertThatThrownBy(() -> vendorManager.updateInfo(vendor.getId().toUuid(), request, requestId))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void delete() {
    when(permissionChecker.hasManagePermission(any(), any(HubId.class))).thenReturn(true);

    Vendor vendor = registerVendor();

    vendorManager.delete(vendor.getId().toUuid(), "requestUser", requestId);
    entityManager.flush();
    entityManager.clear();

    vendor = vendorFinder.find(vendor.getId().toUuid());

    assertThat(vendor.getDeletedBy()).isEqualTo("requestUser");
    assertThat(vendor.getDeletedAt()).isNotNull();
  }

  @Test
  void deleteIfHubNotExists() {
    when(permissionChecker.hasManagePermission(any(), any(HubId.class))).thenReturn(false);

    Vendor vendor = registerVendor();

    assertThatThrownBy(
            () -> vendorManager.delete(vendor.getId().toUuid(), "requestUser", requestId))
        .isInstanceOf(PermissionException.class);
  }

  private Vendor registerVendor() {
    when(permissionChecker.hasRegisterPermission(any())).thenReturn(true);
    when(hubExistenceChecker.hasHub(any())).thenReturn(true);

    Vendor vendor = vendorRegister.register(createRegisterRequest(), requestId);
    entityManager.flush();
    entityManager.clear();

    return vendor;
  }
}
