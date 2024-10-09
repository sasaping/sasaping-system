package com.sparta.promotion.server.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.promotion.server.application.service.CouponService;
import com.sparta.promotion.server.domain.model.Coupon;
import com.sparta.promotion.server.domain.model.vo.CouponType;
import com.sparta.promotion.server.domain.model.vo.DiscountType;
import com.sparta.promotion.server.domain.repository.CouponRepository;
import com.sparta.promotion.server.presentation.request.CouponRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

// TODO(경민): 이벤트 테이블 생성 후 테스트 로직 수정 필요
@SpringBootTest
class CouponServiceTests {

  @MockBean
  private CouponRepository couponRepository;

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

}
