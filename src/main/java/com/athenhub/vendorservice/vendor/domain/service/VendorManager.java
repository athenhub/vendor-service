package com.athenhub.vendorservice.vendor.domain.service;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.vo.VendorId;
import com.athenhub.vendorservice.vendor.domain.vo.request.VendorUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface VendorManager {
  Vendor updateInfo(@NotNull VendorId vendorId, @Valid VendorUpdateRequest updateRequest);
}
