package com.athenhub.vendorservice.vendor.application.service;

import static com.athenhub.vendorservice.vendor.VendorFixture.createRegisterRequest;
import static com.athenhub.vendorservice.vendor.VendorFixture.createUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorUpdateRequest;
import com.athenhub.vendorservice.vendor.domain.service.HubExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.MemberExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.PermissionChecker;
import com.athenhub.vendorservice.vendor.domain.vo.Address;
import com.athenhub.vendorservice.vendor.domain.vo.Coordinate;
import com.athenhub.vendorservice.vendor.domain.vo.HubId;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
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

  @MockitoBean private MemberExistenceChecker memberExistenceChecker;

  Vendor vendor;

  private final UUID requestId = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    vendor = registerVendor();
  }

  @Test
  void updateInfoInfo() {
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
        .isEqualTo(Address.of(request.streetAddress(), request.detailAddress()));
    assertThat(vendor.getCoordinate())
        .isEqualTo(Coordinate.of(request.latitude(), request.longitude()));
  }

  @Test
  void delete() {
    when(permissionChecker.hasManagePermission(any(), any(HubId.class))).thenReturn(true);

    vendorManager.delete(vendor.getId().toUuid(), "requestUser", requestId);
    entityManager.flush();
    entityManager.clear();

    vendor = vendorFinder.find(vendor.getId().toUuid());

    assertThat(vendor.getDeletedBy()).isEqualTo("requestUser");
    assertThat(vendor.getDeletedAt()).isNotNull();
  }

  @Test
  void changeAgent() {
    when(permissionChecker.hasManagePermission(any(), any(HubId.class))).thenReturn(true);
    when(memberExistenceChecker.hasMember(any(UUID.class))).thenReturn(true);

    UUID newAgentId = UUID.randomUUID();
    vendorManager.changeAgent(vendor.getId().toUuid(), newAgentId, requestId);
    entityManager.flush();
    entityManager.clear();

    vendor = vendorFinder.find(vendor.getId().toUuid());

    assertThat(vendor.getAgentId().toUuid()).isEqualTo(newAgentId);
  }

  private Vendor registerVendor() {
    when(permissionChecker.hasRegisterPermission(any(), any(HubId.class))).thenReturn(true);
    when(hubExistenceChecker.hasHub(any())).thenReturn(true);
    when(memberExistenceChecker.hasMember(any(UUID.class))).thenReturn(true);

    Vendor vendor = vendorRegister.register(createRegisterRequest(), requestId);
    entityManager.flush();
    entityManager.clear();

    return vendor;
  }
}
