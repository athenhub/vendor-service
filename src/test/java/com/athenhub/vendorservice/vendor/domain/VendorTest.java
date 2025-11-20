package com.athenhub.vendorservice.vendor.domain;

import static com.athenhub.vendorservice.vendor.VendorFixture.create;
import static com.athenhub.vendorservice.vendor.VendorFixture.createRegisterRequest;
import static com.athenhub.vendorservice.vendor.VendorFixture.createUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;

import com.athenhub.vendorservice.vendor.domain.vo.Address;
import com.athenhub.vendorservice.vendor.domain.vo.HubId;
import com.athenhub.vendorservice.vendor.domain.vo.Location;
import com.athenhub.vendorservice.vendor.domain.vo.request.VendorRegisterRequest;
import com.athenhub.vendorservice.vendor.domain.vo.request.VendorUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VendorTest {

  Vendor vendor;

  @BeforeEach
  void setUp() {
    vendor = create();
  }

  @Test
  void register() {
    VendorRegisterRequest request = createRegisterRequest();
    Vendor vendor = Vendor.register(request);

    assertThat(vendor.getId()).isNotNull();
    assertThat(vendor.getName()).isEqualTo(request.name());
    assertThat(vendor.getType()).isEqualTo(request.type());
    assertThat(vendor.getHubId()).isEqualTo(HubId.of(request.hubId()));
    assertThat(vendor.getAddress())
        .isEqualTo(Address.of(request.address(), request.detailAddress()));
    assertThat(vendor.getLocation())
        .isEqualTo(Location.of(request.latitude(), request.longitude()));
  }

  @Test
  void updateInfo() {
    VendorUpdateRequest request = createUpdateRequest();

    vendor.updateInfo(request);

    assertThat(vendor.getName()).isEqualTo(request.name());
    assertThat(vendor.getType()).isEqualTo(request.type());
    assertThat(vendor.getHubId()).isEqualTo(HubId.of(request.hubId()));
    assertThat(vendor.getAddress())
        .isEqualTo(Address.of(request.address(), request.detailAddress()));
    assertThat(vendor.getLocation())
        .isEqualTo(Location.of(request.latitude(), request.longitude()));
  }

  @Test
  void delete() {
    vendor.delete("test");

    assertThat(vendor.getDeletedBy()).isEqualTo("test");
    assertThat(vendor.getDeletedAt()).isNotNull();
  }
}
