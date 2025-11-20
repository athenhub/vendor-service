package com.athenhub.vendorservice.vendor.domain.service;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.vo.VendorId;

public interface VendorFinder {
  Vendor find(VendorId id);
}
