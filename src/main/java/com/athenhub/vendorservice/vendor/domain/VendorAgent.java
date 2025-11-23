package com.athenhub.vendorservice.vendor.domain;

import com.athenhub.vendorservice.global.domain.AbstractAuditEntity;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorAgentRegisterRequest;
import com.athenhub.vendorservice.vendor.domain.vo.VendorAgentId;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Table(name = "p_vendor_agent")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VendorAgent extends AbstractAuditEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private VendorAgentId agentId;

  private String name;

  private String username;

  private String slackId  ;

  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  @JoinColumn(name = "vendor_id")
  private Vendor vendor;

  public static VendorAgent register(VendorAgentRegisterRequest registerRequest) {
    VendorAgent vendorAgent = new VendorAgent();

    vendorAgent.agentId = VendorAgentId.of(registerRequest.id());
    vendorAgent.name = Objects.requireNonNull(registerRequest.name());
    vendorAgent.username = Objects.requireNonNull(registerRequest.username());
    vendorAgent.slackId = Objects.requireNonNull(registerRequest.slackId());

    return vendorAgent;
  }

  public void assignTo(Vendor vendor) {
    this.vendor = vendor;
  }
}
