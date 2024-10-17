package com.sparta.promotion.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.promotion.server.application.service.CouponService;
import com.sparta.promotion.server.domain.model.Coupon;
import com.sparta.promotion.server.domain.model.UserCoupon;
import com.sparta.promotion.server.domain.model.vo.CouponType;
import com.sparta.promotion.server.domain.model.vo.DiscountType;
import com.sparta.promotion.server.domain.repository.CouponRepository;
import com.sparta.promotion.server.domain.repository.UserCouponRepository;
import com.sparta.promotion.server.exception.PromotionErrorCode;
import com.sparta.promotion.server.exception.PromotionException;
import com.sparta.promotion.server.presentation.request.CouponRequest;
import com.sparta.promotion.server.presentation.response.CouponResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

// TODO(경민): 이벤트 테이블 생성 후 테스트 로직 수정 필요
@SpringBootTest
class CouponServiceTests {

  @MockBean
  private CouponRepository couponRepository;

  @MockBean
  private UserCouponRepository userCouponRepository;

  /*
  @MockBean
  private EventRepository eventRepository;
   */

  @Autowired
  private CouponService couponService;

  @Test
  void test_이벤트_쿠폰_생성() {
    // given
    CouponRequest.Create request = new CouponRequest.Create(
        "Test Coupon",
        CouponType.EVENT,
        DiscountType.PRICE,
        new BigDecimal("1000.00"),
        null,
        null,
        100,
        Timestamp.valueOf("2024-01-01 00:00:00"),
        Timestamp.valueOf("2024-12-31 23:59:59"),
        null,
        1L
    );
    // Event event = new Event();  // 임의의 이벤트 객체 생성

    // when
    // Mockito.when(eventRepository.findById(request.getEventId())).thenReturn(Optional.of(event)); // 이벤트 조회 시 임의의 이벤트 반환
    when(couponRepository.save(any(Coupon.class))).thenReturn(Coupon.create(request));

    // then
    couponService.createEventCoupon(request);

    verify(couponRepository).save(Mockito.any(Coupon.class));
  }

  @Test
  void test_이벤트_쿠폰_실패() {
    // given
    CouponRequest.Create request = new CouponRequest.Create(
        "Test Coupon",
        CouponType.EVENT,
        DiscountType.PRICE,
        new BigDecimal("1000.00"),
        null,
        null,
        100,
        Timestamp.valueOf("2024-01-01 00:00:00"),
        Timestamp.valueOf("2024-12-31 23:59:59"),
        null,
        1L
    );

    // when
    // when(eventRepository.findById(request.getEventId())).thenReturn(Optional.empty());

    // PromotionException exception = assertThrows(PromotionException.class, () -> couponService.createEventCoupon(request));
    // assertEquals(PromotionErrorCode.EVENT_NOT_FOUND.getMessage(), exception.getMessage());
  }

  @Test
  void test_이벤트_쿠폰_제공_성공() {
    // given
    Long userId = 1L;
    Long couponId = 100L;

    Coupon coupon = mock(Coupon.class);
    when(coupon.getQuantity()).thenReturn(10);

    UserCoupon userCoupon = UserCoupon.create(userId, couponId);

    // when
    when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));
    when(userCouponRepository.save(any(UserCoupon.class))).thenReturn(userCoupon);

    // then
    couponService.provideEventCoupon(userId, couponId);

    verify(couponRepository).findById(couponId);
    verify(coupon).updateQuantity(anyInt());
    verify(userCouponRepository).save(any(UserCoupon.class));
  }

  @Test
  void test_이벤트_쿠폰_실패_수량부족() {
    // given
    Long userId = 1L;
    Long couponId = 100L;

    Coupon coupon = mock(Coupon.class);
    when(coupon.getQuantity()).thenReturn(0); // 수량이 0일 경우

    // when
    when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));

    // then
    PromotionException exception = assertThrows(PromotionException.class, () -> {
      couponService.provideEventCoupon(userId, couponId);
    });

    assertEquals(PromotionErrorCode.INSUFFICIENT_COUPON.getMessage(), exception.getMessage());
    verify(couponRepository).findById(couponId);
    verify(coupon, never()).updateQuantity(anyInt());
    verify(userCouponRepository, never()).save(any(UserCoupon.class));
  }

  @Test
  void test_이벤트_쿠폰_실패_쿠폰없음() {
    // given
    Long userId = 1L;
    Long couponId = 100L;

    // when
    when(couponRepository.findById(couponId)).thenReturn(Optional.empty());

    // then
    PromotionException exception = assertThrows(PromotionException.class, () -> {
      couponService.provideEventCoupon(userId, couponId);
    });

    assertEquals(PromotionErrorCode.COUPON_NOT_FOUND.getMessage(), exception.getMessage());
    verify(couponRepository).findById(couponId);
    verify(userCouponRepository, never()).save(any(UserCoupon.class)); // 저장이 일어나지 않음
  }

  @Test
  void test_쿠폰_전체_조회() {
    // given
    Coupon coupon1 = new Coupon(1L, "Coupon 1", CouponType.EVENT, DiscountType.PERCENTAGE,
        new BigDecimal("10.00"), new BigDecimal("100.00"), new BigDecimal("500.00"), 100,
        Timestamp.valueOf("2024-01-01 00:00:00"), Timestamp.valueOf("2024-12-31 23:59:59"),
        null, null);

    Coupon coupon2 = new Coupon(2L, "Coupon 2", CouponType.EVENT, DiscountType.PRICE,
        new BigDecimal("500.00"), null, null, 50,
        Timestamp.valueOf("2024-02-01 00:00:00"), Timestamp.valueOf("2024-12-31 23:59:59"),
        "VIP", 2L);

    List<Coupon> coupons = List.of(coupon1, coupon2);
    Pageable pageable = PageRequest.of(0, 10);
    Page<Coupon> couponPage = new PageImpl<>(coupons, pageable, coupons.size());

    when(couponRepository.findAll(pageable)).thenReturn(couponPage);

    // when
    Page<CouponResponse.Get> responsePage = couponService.getCouponList(pageable);

    // then
    assertEquals(2, responsePage.getTotalElements());
    assertEquals("Coupon 1", responsePage.getContent().get(0).getName());
    assertEquals("Coupon 2", responsePage.getContent().get(1).getName());

    verify(couponRepository).findAll(pageable);
  }

  @Test
  void test_단일_쿠폰_조회_성공() {
    // given
    Long couponId = 1L;
    Coupon coupon = new Coupon(couponId, "Test Coupon", CouponType.EVENT, DiscountType.PRICE,
        new BigDecimal("1000.00"), null, null, 100,
        Timestamp.valueOf("2024-01-01 00:00:00"), Timestamp.valueOf("2024-12-31 23:59:59"),
        null, null);

    when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));

    // when
    CouponResponse.Get response = couponService.getCoupon(couponId);

    // then
    assertEquals(couponId, response.getCouponId());
    assertEquals("Test Coupon", response.getName());
    verify(couponRepository).findById(couponId);
  }

  @Test
  void test_단일_쿠폰_조회_실패() {
    // given
    Long couponId = 100L;

    when(couponRepository.findById(couponId)).thenReturn(Optional.empty());

    // when & then
    PromotionException exception = assertThrows(PromotionException.class, () -> {
      couponService.getCoupon(couponId);
    });

    assertEquals(PromotionErrorCode.COUPON_NOT_FOUND.getMessage(), exception.getMessage());
    verify(couponRepository).findById(couponId);
  }

  @Test
  void test_특정_사용자의_아이디로_쿠폰_조회_성공() {
    // given
    Long userId = 1L;
    Pageable pageable = PageRequest.of(0, 10);

    UserCoupon userCoupon = UserCoupon.create(userId, 1L);
    List<UserCoupon> userCouponList = Arrays.asList(userCoupon);
    Page<UserCoupon> userCouponPage = new PageImpl<>(userCouponList, pageable,
        userCouponList.size());

    Coupon coupon = new Coupon(1L, "Test Coupon", CouponType.EVENT, DiscountType.PRICE,
        new BigDecimal("1000.00"), null, null, 100,
        Timestamp.valueOf("2024-01-01 00:00:00"), Timestamp.valueOf("2024-12-31 23:59:59"),
        null, null);

    when(userCouponRepository.findByUserId(userId, pageable)).thenReturn(userCouponPage);
    when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon));

    // when
    Page<CouponResponse.Get> response = couponService.getCouponListBoyUserId(userId, pageable);

    // then
    assertEquals(1, response.getTotalElements());
    assertEquals("Test Coupon", response.getContent().get(0).getName());
    verify(userCouponRepository).findByUserId(userId, pageable);
    verify(couponRepository).findById(1L);
  }

  @Test
  void test_특정_사용자의_아이디로_쿠폰_조회_실패_쿠폰없음() {
    // given
    Long userId = 1L;
    Pageable pageable = PageRequest.of(0, 10);

    UserCoupon userCoupon = UserCoupon.create(userId, 1L); // create 메서드를 사용해 생성
    List<UserCoupon> userCouponList = Arrays.asList(userCoupon);
    Page<UserCoupon> userCouponPage = new PageImpl<>(userCouponList, pageable,
        userCouponList.size());

    when(userCouponRepository.findByUserId(userId, pageable)).thenReturn(userCouponPage);
    when(couponRepository.findById(1L)).thenReturn(Optional.empty()); // 쿠폰을 찾을 수 없는 경우

    // when & then
    PromotionException exception = assertThrows(PromotionException.class, () -> {
      couponService.getCouponListBoyUserId(userId, pageable);
    });

    assertEquals(PromotionErrorCode.COUPON_NOT_FOUND.getMessage(), exception.getMessage());
    verify(userCouponRepository).findByUserId(userId, pageable);
    verify(couponRepository).findById(1L);
  }

}
