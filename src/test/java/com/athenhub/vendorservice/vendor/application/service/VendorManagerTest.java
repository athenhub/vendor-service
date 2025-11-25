package com.athenhub.vendorservice.vendor.application.service;

import static com.athenhub.vendorservice.vendor.VendorFixture.createRegisterRequest;
import static com.athenhub.vendorservice.vendor.VendorFixture.createUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorUpdateRequest;
import com.athenhub.vendorservice.vendor.domain.event.VendorAgentChanged;
import com.athenhub.vendorservice.vendor.domain.event.VendorDeleted;
import com.athenhub.vendorservice.vendor.domain.event.VendorRegistered;
import com.athenhub.vendorservice.vendor.domain.event.VendorUpdated;
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

  @MockitoBean private VendorEventPublisher vendorEventPublisher;

  Vendor vendor;

  private final UUID requestId = UUID.randomUUID();
  private final String requestUser = "requestUser";

  @BeforeEach
  void setUp() {
    vendor = registerVendor();
  }

  @Test
  void updateInfoInfo() {
    VendorUpdateRequest request = createUpdateRequest();

    when(permissionChecker.hasUpdatePermission(any(), any(HubId.class), any())).thenReturn(true);
    when(hubExistenceChecker.hasHub(any(HubId.class))).thenReturn(true);
    doNothing().when(vendorEventPublisher).publish(any(VendorUpdated.class));

    vendorManager.updateInfo(vendor.getId().toUuid(), request, requestId, requestUser);
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
    verify(vendorEventPublisher).publish(any(VendorUpdated.class));
  }

  @Test
  void delete() {
    when(permissionChecker.hasDeletePermission(any(), any(HubId.class))).thenReturn(true);
    doNothing().when(vendorEventPublisher).publish(any(VendorDeleted.class));

    vendorManager.delete(vendor.getId().toUuid(), "requestUser", requestId, requestUser);
    entityManager.flush();
    entityManager.clear();

    vendor = vendorFinder.find(vendor.getId().toUuid());

    assertThat(vendor.getDeletedBy()).isEqualTo("requestUser");
    assertThat(vendor.getDeletedAt()).isNotNull();
    verify(vendorEventPublisher).publish(any(VendorDeleted.class));
  }

  @Test
  void changeAgent() {
    when(permissionChecker.hasUpdatePermission(any(), any(HubId.class), any())).thenReturn(true);
    when(memberExistenceChecker.hasMember(any(UUID.class))).thenReturn(true);
    doNothing().when(vendorEventPublisher).publish(any(VendorAgentChanged.class));

    UUID newAgentId = UUID.randomUUID();
    vendorManager.changeAgent(vendor.getId().toUuid(), newAgentId, requestId, requestUser);
    entityManager.flush();
    entityManager.clear();

    vendor = vendorFinder.find(vendor.getId().toUuid());

    assertThat(vendor.getAgentId().toUuid()).isEqualTo(newAgentId);
    verify(vendorEventPublisher).publish(any(VendorAgentChanged.class));
  }

  private Vendor registerVendor() {
    when(permissionChecker.hasRegisterPermission(any(), any(HubId.class))).thenReturn(true);
    when(hubExistenceChecker.hasHub(any())).thenReturn(true);
    when(memberExistenceChecker.hasMember(any(UUID.class))).thenReturn(true);
    doNothing().when(vendorEventPublisher).publish(any(VendorRegistered.class));

    Vendor vendor = vendorRegister.register(createRegisterRequest(), requestId, requestUser);
    entityManager.flush();
    entityManager.clear();

    return vendor;
  }
}
