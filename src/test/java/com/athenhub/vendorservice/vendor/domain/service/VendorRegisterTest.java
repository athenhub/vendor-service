package com.athenhub.vendorservice.vendor.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.athenhub.vendorservice.vendor.VendorFixture;
import com.athenhub.vendorservice.vendor.domain.Vendor;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
record VendorRegisterTest(
    VendorRegister vendorRegister, VendorFinder vendorFinder, EntityManager entityManager) {

  @Test
  void register() {
    Vendor vendor = vendorRegister.register(VendorFixture.createRegisterRequest());

    assertThat(vendor.getId()).isNotNull();
  }
}
