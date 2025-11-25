package com.athenhub.vendorservice.vendor.application.service;

import com.athenhub.vendorservice.vendor.domain.event.VendorAgentChanged;
import com.athenhub.vendorservice.vendor.domain.event.VendorDeleted;
import com.athenhub.vendorservice.vendor.domain.event.VendorRegistered;
import com.athenhub.vendorservice.vendor.domain.event.VendorUpdated;

public interface VendorEventPublisher {
  void publish(VendorRegistered event);

  void publish(VendorUpdated event);

  void publish(VendorDeleted event);

  void publish(VendorAgentChanged event);
}
