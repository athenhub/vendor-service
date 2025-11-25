package com.athenhub.vendorservice.vendor.domain.event;

import java.util.UUID;

public record VendorAgentChanged(
    UUID vendorId,
    String vendorName,
    UUID oldVendorAgentId,
    UUID newVendorAgentId,
    String requestUsername) {}
