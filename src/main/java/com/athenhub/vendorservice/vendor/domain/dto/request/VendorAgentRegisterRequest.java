package com.athenhub.vendorservice.vendor.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record VendorAgentRegisterRequest(
    @NotNull UUID id,
    @NotBlank String name,
    @NotBlank String username,
    @NotBlank String slackId
) {
  public static VendorAgentRegisterRequest of(VendorAgentChangeRequest changeRequest) {
    return new VendorAgentRegisterRequest(
        changeRequest.id(),
        changeRequest.name(),
        changeRequest.username(),
        changeRequest.slackId()
    );
  }
}
