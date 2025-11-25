package com.athenhub.vendorservice.vendor.domain.event;

import java.util.UUID;

public record VendorUpdated(String vendorName, UUID vendorAgentId, String requestUsername) {}
