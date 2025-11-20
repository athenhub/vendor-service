package com.athenhub.vendorservice.vendor.domain.service;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.VendorRepository;
import com.athenhub.vendorservice.vendor.domain.vo.VendorId;
import com.athenhub.vendorservice.vendor.domain.vo.request.VendorRegisterRequest;
import com.athenhub.vendorservice.vendor.domain.vo.request.VendorUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class VendorManageService implements VendorRegister, VendorManager{

  private final VendorRepository vendorRepository;
  private final VendorFinder vendorFinder;

  @Override
  public Vendor register(VendorRegisterRequest registerRequest) {
    Vendor vendor = Vendor.register(registerRequest);

    return vendorRepository.save(vendor);
  }

  @Override
  public Vendor updateInfo(VendorId id, VendorUpdateRequest updateRequest) {
    Vendor vendor = vendorFinder.find(id);

    vendor.updateInfo(updateRequest);

    return vendorRepository.save(vendor);
  }
}
