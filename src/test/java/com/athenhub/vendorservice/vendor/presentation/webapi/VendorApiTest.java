package com.athenhub.vendorservice.vendor.presentation.webapi;

import static com.athenhub.vendorservice.AssertThatUtils.isEqualTo;
import static com.athenhub.vendorservice.vendor.VendorFixture.create;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.athenhub.vendorservice.MockUser;
import com.athenhub.vendorservice.vendor.VendorFixture;
import com.athenhub.vendorservice.vendor.application.service.VendorFinder;
import com.athenhub.vendorservice.vendor.application.service.VendorManager;
import com.athenhub.vendorservice.vendor.application.service.VendorRegister;
import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorRegisterRequest;
import com.athenhub.vendorservice.vendor.domain.dto.request.VendorUpdateRequest;
import com.athenhub.vendorservice.vendor.domain.service.HubExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.MemberExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.PermissionChecker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@SpringBootTest
@AutoConfigureMockMvc
class VendorApiTest {

  @Autowired MockMvcTester mvcTester;

  @Autowired ObjectMapper objectMapper;

  @MockitoBean VendorRegister vendorRegister;

  @MockitoBean VendorManager vendorManager;

  @MockitoBean VendorFinder vendorFinder;

  @MockitoBean PermissionChecker permissionChecker;

  @MockitoBean HubExistenceChecker hubExistenceChecker;

  @MockitoBean MemberExistenceChecker memberExistenceChecker;

  Vendor vendor;

  private final UUID requestId = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    vendor = create(permissionChecker, hubExistenceChecker, memberExistenceChecker);
  }

  @Test
  @MockUser(roles = "MASTER_MANAGER")
  void register() throws JsonProcessingException {
    VendorRegisterRequest request = VendorFixture.createRegisterRequest();

    vendor =
        VendorFixture.create(
            request, permissionChecker, hubExistenceChecker, memberExistenceChecker);
    given(vendorRegister.register(any(VendorRegisterRequest.class), any())).willReturn(vendor);
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result =
        mvcTester
            .post()
            .uri("/v1/vendors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .exchange();

    assertThat(result)
        .hasStatusOk()
        .bodyJson()
        .hasPathSatisfying("$.vendorId", isEqualTo(vendor.getId().toString()));
  }

  @Test
  @MockUser(roles = "VENDOR_AGENT")
  void registerIfUnauthorized() throws JsonProcessingException {
    VendorRegisterRequest request = VendorFixture.createRegisterRequest();

    Vendor vendor =
        VendorFixture.create(
            request, permissionChecker, hubExistenceChecker, memberExistenceChecker);
    given(vendorRegister.register(any(VendorRegisterRequest.class), any())).willReturn(vendor);
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result =
        mvcTester
            .post()
            .uri("/v1/vendors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .exchange();

    assertThat(result).hasStatus(HttpStatus.FORBIDDEN);
  }

  @Test
  @MockUser(roles = "MASTER_MANAGER")
  void find() {
    Vendor vendor =
        VendorFixture.create(permissionChecker, hubExistenceChecker, memberExistenceChecker);
    given(vendorFinder.find(any())).willReturn(vendor);

    MvcTestResult result =
        mvcTester.get().uri("/v1/vendors/{vendorId}", vendor.getId().toString()).exchange();

    assertThat(result)
        .hasStatusOk()
        .bodyJson()
        .hasPathSatisfying("$.vendorId", isEqualTo(vendor.getId().toString()));
  }

  @Test
  @MockUser(roles = "MASTER_MANAGER")
  void update() throws JsonProcessingException {
    VendorUpdateRequest request = VendorFixture.createUpdateRequest();

    Vendor vendor =
        VendorFixture.create(permissionChecker, hubExistenceChecker, memberExistenceChecker);
    given(vendorManager.updateInfo(any(), any(VendorUpdateRequest.class), any()))
        .willReturn(vendor);
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result =
        mvcTester
            .put()
            .uri("/v1/vendors/{vendorId}", vendor.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .exchange();

    assertThat(result)
        .hasStatusOk()
        .bodyJson()
        .hasPathSatisfying("$.vendorId", isEqualTo(vendor.getId().toString()));
  }

  @Test
  @MockUser(roles = "VENDOR_AGENT")
  void updateIfUnauthorized() throws JsonProcessingException {
    VendorUpdateRequest request = VendorFixture.createUpdateRequest();

    Vendor vendor =
        VendorFixture.create(permissionChecker, hubExistenceChecker, memberExistenceChecker);
    given(vendorManager.updateInfo(any(), any(VendorUpdateRequest.class), any()))
        .willReturn(vendor);
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result =
        mvcTester
            .put()
            .uri("/v1/vendors/{vendorId}", vendor.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .exchange();

    assertThat(result).hasStatus(HttpStatus.FORBIDDEN);
  }

  @Test
  @MockUser(roles = "MASTER_MANAGER")
  void delete() {
    Vendor vendor =
        VendorFixture.create(permissionChecker, hubExistenceChecker, memberExistenceChecker);
    given(vendorManager.delete(any(), anyString(), any())).willReturn(vendor);

    MvcTestResult result =
        mvcTester.delete().uri("/v1/vendors/{vendorId}", vendor.getId().toString()).exchange();

    assertThat(result)
        .hasStatusOk()
        .bodyJson()
        .hasPathSatisfying("$.vendorId", isEqualTo(vendor.getId().toString()));
  }

  @Test
  @MockUser(roles = "VENDOR_AGENT")
  void deleteIfUnauthorized() {
    Vendor vendor =
        VendorFixture.create(permissionChecker, hubExistenceChecker, memberExistenceChecker);
    given(vendorManager.delete(any(), anyString(), any())).willReturn(vendor);

    MvcTestResult result =
        mvcTester.delete().uri("/v1/vendors/{vendorId}", vendor.getId().toString()).exchange();

    assertThat(result).hasStatus(HttpStatus.FORBIDDEN);
  }
}
