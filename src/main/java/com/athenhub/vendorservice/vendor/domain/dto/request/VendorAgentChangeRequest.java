package com.athenhub.vendorservice.vendor.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record VendorAgentChangeRequest(
    @NotNull UUID id,
    @NotBlank String name,
    @NotBlank String username,
    @NotBlank String slackId
) {
}
