package com.athenhub.vendorservice.vendor.domain.service;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.vo.request.VendorRegisterRequest;
import jakarta.validation.Valid;

public interface VendorRegister {
  Vendor register(@Valid VendorRegisterRequest registerRequest);
}
