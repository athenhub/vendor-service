package com.athenhub.vendorservice.vendor.application.service;

import static com.athenhub.vendorservice.vendor.VendorFixture.createRegisterRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.athenhub.vendorservice.vendor.domain.Vendor;
import com.athenhub.vendorservice.vendor.domain.VendorType;
import com.athenhub.vendorservice.vendor.domain.dto.VendorSearchCondition;
import com.athenhub.vendorservice.vendor.domain.event.VendorDeleted;
import com.athenhub.vendorservice.vendor.domain.event.VendorRegistered;
import com.athenhub.vendorservice.vendor.domain.service.HubExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.MemberExistenceChecker;
import com.athenhub.vendorservice.vendor.domain.service.PermissionChecker;
import com.athenhub.vendorservice.vendor.domain.vo.HubId;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VendorFinderTest {

  @Autowired private VendorRegister vendorRegister;

  @Autowired private VendorFinder vendorFinder;

  @Autowired private VendorManager vendorManager;

  @Autowired private EntityManager entityManager;

  @MockitoBean private PermissionChecker permissionChecker;

  @MockitoBean private HubExistenceChecker hubExistenceChecker;

  @MockitoBean private MemberExistenceChecker memberExistenceChecker;

  @MockitoBean private VendorEventPublisher vendorEventPublisher;

  private final UUID requestId = UUID.randomUUID();

  Vendor vendor1;
  Vendor vendor2;
  Vendor vendor3;
  Vendor vendor4;
  Vendor vendor5;
  Vendor vendor6;
  Vendor vendor7;
  Vendor vendor8;
  Vendor vendor9;
  Vendor vendor10;

  UUID hubId1 = UUID.randomUUID();
  UUID hubId2 = UUID.randomUUID();

  @BeforeAll
  void setUpAll() {
    vendor1 = registerVendor("테스트 업체1", hubId1, VendorType.RECEIVER, "서울시 테스트로 1", "1호");
    vendor2 = registerVendor("테스트 업체2", hubId2, VendorType.RECEIVER, "서울시 테스트로 1", "1호");
    vendor3 = registerVendor("테스트 업체3", hubId1, VendorType.RECEIVER, "인천시 테스트로 1", "1호");
    vendor4 = registerVendor("아테네 업체4", hubId2, VendorType.RECEIVER, "서울시 테스트로 1", "1호");
    vendor5 = registerVendor("아테네 업체5", hubId1, VendorType.RECEIVER, "인천시 테스트로 1", "1호");
    vendor6 = registerVendor("테스트 업체6", hubId2, VendorType.PRODUCER, "인천시 테스트로 1", "1호");
    vendor7 = registerVendor("테스트 업체7", hubId1, VendorType.PRODUCER, "서울시 테스트로 1", "A호");
    vendor8 = registerVendor("테스트 업체8", hubId1, VendorType.PRODUCER, "서울시 테스트로 1", "A호");
    vendor9 = registerVendor("테스트 업체9", hubId1, VendorType.PRODUCER, "서울시 테스트로 1", "A호");
    vendor10 = registerVendor("테스트 업체10", hubId2, VendorType.PRODUCER, "서울시 테스트로 1", "A호");

    when(permissionChecker.hasDeletePermission(any(), any(HubId.class))).thenReturn(true);
    doNothing().when(vendorEventPublisher).publish(any(VendorDeleted.class));
    vendorManager.delete(vendor1.getId().toUuid(), "test", UUID.randomUUID(), "testUser");
    vendorManager.delete(vendor2.getId().toUuid(), "test", UUID.randomUUID(), "testUser");
  }

  @BeforeEach
  void setUp() {
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @DisplayName("미삭제 필터 테스트")
  void findVendorsByNotDeleted() {
    VendorSearchCondition condition = VendorSearchCondition.of(null, null, null, false);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Vendor> vendors = vendorFinder.search(condition, pageable);

    assertThat(vendors.getContent())
        .containsExactlyInAnyOrder(
            vendor3, vendor4, vendor5, vendor6, vendor7, vendor8, vendor9, vendor10);
  }

  @Test
  @DisplayName("타입 필터 테스트")
  void findVendorsByType() {
    VendorSearchCondition condition =
        VendorSearchCondition.of(VendorType.PRODUCER, null, null, true);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Vendor> vendors = vendorFinder.search(condition, pageable);

    assertThat(vendors.getContent())
        .containsExactlyInAnyOrder(vendor6, vendor7, vendor8, vendor9, vendor10);
  }

  @Test
  @DisplayName("허브 식별자 필터 테스트")
  void findVendorsByHubId() {
    VendorSearchCondition condition = VendorSearchCondition.of(null, HubId.of(hubId2), null, true);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Vendor> vendors = vendorFinder.search(condition, pageable);

    assertThat(vendors.getContent()).containsExactlyInAnyOrder(vendor2, vendor4, vendor6, vendor10);
  }

  @Test
  @DisplayName("이름 검색 테스트")
  void findVendorsByName() {
    VendorSearchCondition condition = VendorSearchCondition.of(null, null, "아테네", true);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Vendor> vendors = vendorFinder.search(condition, pageable);

    assertThat(vendors.getContent()).containsExactlyInAnyOrder(vendor4, vendor5);
  }

  @Test
  @DisplayName("기본주소 검색 테스트")
  void findVendorsByStreetAddress() {
    VendorSearchCondition condition = VendorSearchCondition.of(null, null, "인천", true);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Vendor> vendors = vendorFinder.search(condition, pageable);

    assertThat(vendors.getContent()).containsExactlyInAnyOrder(vendor3, vendor5, vendor6);
  }

  @Test
  @DisplayName("상세주소 검색 테스트")
  void findVendorsByDetailAddress() {
    VendorSearchCondition condition = VendorSearchCondition.of(null, null, "A", true);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Vendor> vendors = vendorFinder.search(condition, pageable);

    assertThat(vendors.getContent()).containsExactlyInAnyOrder(vendor7, vendor8, vendor9, vendor10);
  }

  @Test
  @DisplayName("정렬 테스트")
  void findVendorsWithOrder() {
    VendorSearchCondition condition = VendorSearchCondition.of(null, null, null, true);
    Pageable pageable = PageRequest.of(0, 10, Direction.DESC, "name");

    Page<Vendor> vendors = vendorFinder.search(condition, pageable);

    assertThat(vendors.getContent())
        .containsExactly(
            vendor9, vendor8, vendor7, vendor6, vendor3, vendor2, vendor10, vendor1, vendor5,
            vendor4);
  }

  @Test
  @DisplayName("페이징 테스트")
  void findVendorsWithPaging() {
    VendorSearchCondition condition = VendorSearchCondition.of(null, null, null, true);
    Pageable pageable = PageRequest.of(1, 2, Direction.DESC, "name");

    Page<Vendor> vendors = vendorFinder.search(condition, pageable);

    assertThat(vendors.getContent()).containsExactly(vendor7, vendor6);
    assertThat(vendors.getTotalElements()).isEqualTo(10);
    assertThat(vendors.getTotalPages()).isEqualTo(5);
  }

  @Test
  @DisplayName("검색 결과가 없는 경우")
  void findVendorsWhenNoMatch() {
    VendorSearchCondition condition = VendorSearchCondition.of(null, null, "없는 이름", true);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Vendor> vendors = vendorFinder.search(condition, pageable);

    assertThat(vendors.getContent()).isEmpty();
  }

  @Test
  @DisplayName("복합 필터 테스트: 타입 + 허브 + 이름")
  void findVendorsByTypeHubAndName() {
    VendorSearchCondition condition =
        VendorSearchCondition.of(VendorType.PRODUCER, HubId.of(hubId1), "테스트", true);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Vendor> vendors = vendorFinder.search(condition, pageable);

    assertThat(vendors.getContent()).containsExactlyInAnyOrder(vendor7, vendor8, vendor9);
  }

  @Test
  @DisplayName("검색어 대소문자 테스트")
  void findVendorsByLetterCase() {
    VendorSearchCondition condition = VendorSearchCondition.of(null, null, "a", true);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Vendor> vendors = vendorFinder.search(condition, pageable);

    assertThat(vendors.getContent()).containsExactlyInAnyOrder(vendor7, vendor8, vendor9, vendor10);
  }

  private Vendor registerVendor(
      String name, UUID hubId, VendorType type, String streetAddress, String detailAddress) {
    when(permissionChecker.hasRegisterPermission(any(), any(HubId.class))).thenReturn(true);
    when(hubExistenceChecker.hasHub(any())).thenReturn(true);
    when(memberExistenceChecker.hasMember(any(UUID.class))).thenReturn(true);
    doNothing().when(vendorEventPublisher).publish(any(VendorRegistered.class));

    return vendorRegister.register(
        createRegisterRequest(name, hubId, type, streetAddress, detailAddress, UUID.randomUUID()),
        requestId, "requestUser");
  }
}
