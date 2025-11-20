package com.athenhub.vendorservice.vendor.domain;

import com.athenhub.vendorservice.vendor.domain.vo.VendorId;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface VendorRepository extends Repository<Vendor, VendorId> {
  Vendor save(Vendor vendor);

  Optional<Vendor> findById(VendorId id);
}
