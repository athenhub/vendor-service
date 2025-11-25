package com.athenhub.vendorservice.vendor.domain.event;

import java.util.UUID;

public record VendorDeleted(UUID vendorAgentId, String requestUsername) {}
