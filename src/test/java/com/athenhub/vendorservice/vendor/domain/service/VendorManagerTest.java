package com.athenhub.vendorservice.vendor.domain.service;

import static com.athenhub.vendorservice.vendor.VendorFixture.createRegisterRequest;
import static com.athenhub.vendorservice.vendor.VendorFixture.createUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;

import com.athenhub.vendorservice.vendor.application.service.VendorFinder;
import com.athenhub.vendorservice.vendor.application.service.VendorManager;
import com.athenhub.vendorservice.vendor.application.service.VendorRegister;
import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.vo.Address;
import com.athenhub.vendorservice.vendor.domain.vo.Coordinate;
import com.athenhub.vendorservice.vendor.domain.vo.HubId;
import com.athenhub.vendorservice.vendor.domain.vo.request.VendorUpdateRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
record VendorManagerTest(
    VendorRegister vendorRegister,
    VendorManager vendorManager,
    VendorFinder vendorFinder,
    EntityManager entityManager) {
  @Test
  void updateInfoInfo() {
    Vendor vendor = registerVendor();
    VendorUpdateRequest request = createUpdateRequest();

    vendorManager.updateInfo(vendor.getId(), request);
    entityManager.flush();
    entityManager.clear();

    vendor = vendorFinder.find(vendor.getId());

    assertThat(vendor.getName()).isEqualTo(request.name());
    assertThat(vendor.getType()).isEqualTo(request.type());
    assertThat(vendor.getHubId()).isEqualTo(HubId.of(request.hubId()));
    assertThat(vendor.getAddress())
        .isEqualTo(Address.of(request.address(), request.detailAddress()));
    assertThat(vendor.getCoordinate())
        .isEqualTo(Coordinate.of(request.latitude(), request.longitude()));
  }

  @Test
  void delete() {
    Vendor vendor = registerVendor();

    vendorManager.delete(vendor.getId(), "requestUser");
    entityManager.flush();
    entityManager.clear();

    vendor = vendorFinder.find(vendor.getId());

    assertThat(vendor.getDeletedBy()).isEqualTo("requestUser");
    assertThat(vendor.getDeletedAt()).isNotNull();
  }

  private Vendor registerVendor() {
    Vendor vendor = vendorRegister.register(createRegisterRequest());
    entityManager.flush();
    entityManager.clear();

    return vendor;
  }
}
