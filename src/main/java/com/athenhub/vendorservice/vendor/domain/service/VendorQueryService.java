package com.athenhub.vendorservice.vendor.domain.service;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.VendorRepository;
import com.athenhub.vendorservice.vendor.domain.vo.VendorId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VendorQueryService implements VendorFinder {

  private final VendorRepository vendorRepository;

  @Override
  public Vendor find(VendorId id) {
    return vendorRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("업체 정보를 찾을수 없습니다. id: " + id.toString()));
  }
}
