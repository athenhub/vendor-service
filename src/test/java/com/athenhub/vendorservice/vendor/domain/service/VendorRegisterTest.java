package com.athenhub.vendorservice.vendor.domain.service;

import static com.athenhub.vendorservice.vendor.VendorFixture.createRegisterRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.athenhub.vendorservice.vendor.application.service.VendorFinder;
import com.athenhub.vendorservice.vendor.application.service.VendorRegister;
import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.exception.PermissionException;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class VendorRegisterTest {
  @Autowired private VendorRegister vendorRegister;

  @Autowired private VendorFinder vendorFinder;

  @Autowired private EntityManager entityManager;

  @MockitoBean private PermissionChecker permissionChecker;

  @MockitoBean private HubExistenceChecker hubExistenceChecker;

  @Test
  void register() {
    when(permissionChecker.hasRegisterPermission(any())).thenReturn(true);
    when(hubExistenceChecker.hasHub(any())).thenReturn(true);

    Vendor vendor = vendorRegister.register(createRegisterRequest(), UUID.randomUUID());

    assertThat(vendor.getId()).isNotNull();
  }

  @Test
  void registerHasNotPermission() {
    when(permissionChecker.hasRegisterPermission(any())).thenReturn(false);

    assertThatThrownBy(() -> vendorRegister.register(createRegisterRequest(), UUID.randomUUID()))
        .isInstanceOf(PermissionException.class);
  }

  @Test
  void registerIfHubNotExists() {
    when(permissionChecker.hasRegisterPermission(any())).thenReturn(true);
    when(hubExistenceChecker.hasHub(any())).thenReturn(false);

    assertThatThrownBy(() -> vendorRegister.register(createRegisterRequest(), UUID.randomUUID()))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
