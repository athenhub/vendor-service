package com.athenhub.vendorservice.vendor.application.service;

import static com.athenhub.vendorservice.vendor.VendorFixture.createRegisterRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.service.HubExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.PermissionChecker;
import com.athenhub.vendorservice.vendor.domain.vo.HubId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class VendorRegisterTest {
  @Autowired VendorRegister vendorRegister;

  @MockitoBean PermissionChecker permissionChecker;

  @MockitoBean HubExistenceChecker hubExistenceChecker;

  @Test
  void register() {
    when(permissionChecker.hasManagePermission(any(), any(HubId.class))).thenReturn(true);
    when(hubExistenceChecker.hasHub(any(HubId.class))).thenReturn(true);

    Vendor vendor = vendorRegister.register(createRegisterRequest(), UUID.randomUUID());

    assertThat(vendor.getId()).isNotNull();
  }
}
