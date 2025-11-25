package com.athenhub.vendorservice.vendor.domain.event;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import java.util.UUID;

public record VendorRegistered(
    UUID vendorId, String vendorName, UUID vendorAgentId, String requestUsername) {

  public static VendorRegistered from(Vendor vendor, String requestUsername) {
    return new VendorRegistered(
        vendor.getId().toUuid(), vendor.getName(), vendor.getAgentId().toUuid(), requestUsername);
  }
}
