package com.sparta.promotion.server.application.service;

import com.sparta.common.domain.entity.KafkaTopicConstant;
import com.sparta.promotion.server.domain.model.Coupon;
import com.sparta.promotion.server.domain.model.UserCoupon;
import com.sparta.promotion.server.domain.repository.CouponRepository;
import com.sparta.promotion.server.domain.repository.EventRepository;
import com.sparta.promotion.server.domain.repository.UserCouponRepository;
import com.sparta.promotion.server.exception.PromotionErrorCode;
import com.sparta.promotion.server.exception.PromotionException;
import com.sparta.promotion.server.presentation.request.CouponRequest;
import com.sparta.promotion.server.presentation.request.CouponRequest.Update;
import com.sparta.promotion.server.presentation.response.CouponResponse;
import com.sparta.user.user_dto.infrastructure.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CouponService {

  private final CouponRepository couponRepository;
  private final UserCouponRepository userCouponRepository;
  private final EventRepository eventRepository;
  private final UserService userService;
  private final KafkaTemplate<String, CouponRequest.Event> kafkaTemplate;

  @Transactional
  public void createEventCoupon(CouponRequest.Create request) {
    if (request.getEventId() == null) {
      throw new PromotionException(PromotionErrorCode.EVENT_NOT_FOUND);
    }
    eventRepository
        .findById(request.getEventId())
        .orElseThrow(() -> new PromotionException(PromotionErrorCode.EVENT_NOT_FOUND));
    couponRepository.save(Coupon.create(request));
  }

  public void provideEventCouponRequest(Long userId, Long couponId) {
    CouponRequest.Event couponEvent = new CouponRequest.Event(userId, couponId);
    kafkaTemplate.send(KafkaTopicConstant.PROVIDE_EVENT_COUPON, couponEvent);
  }

  @Transactional
  public void provideEventCouponInternal(Long userId, Long couponId) {
    UserDto userData = userService.getUserByUserId(userId);
    if (userData == null) {
      throw new PromotionException(PromotionErrorCode.INTERNAL_SERVER_ERROR);
    }
    Coupon coupon = couponRepository
        .findById(couponId)
        .orElseThrow(() -> new PromotionException(PromotionErrorCode.COUPON_NOT_FOUND));
    if (coupon.getQuantity() - 1 < 0) {
      throw new PromotionException(PromotionErrorCode.INSUFFICIENT_COUPON);
    }
    coupon.updateQuantity(coupon.getQuantity() - 1);
    userCouponRepository.save(UserCoupon.create(userId, couponId));
  }

  public Page<CouponResponse.Get> getCouponList(Pageable pageable) {
    Page<Coupon> coupons = couponRepository.findAll(pageable);
    return coupons.map(CouponResponse.Get::of);
  }

  public CouponResponse.Get getCoupon(Long couponId) {
    Coupon coupon = couponRepository.findById(couponId)
        .orElseThrow(() -> new PromotionException(PromotionErrorCode.COUPON_NOT_FOUND));
    return CouponResponse.Get.of(coupon);
  }

  public Page<CouponResponse.Get> getCouponListBoyUserId(Long userId, Pageable pageable) {
    UserDto userData = userService.getUserByUserId(userId);
    if (userData == null) {
      throw new PromotionException(PromotionErrorCode.INTERNAL_SERVER_ERROR);
    }
    Page<UserCoupon> userCoupons = userCouponRepository.findByUserId(userId, pageable);

    return userCoupons.map(userCoupon -> {
      Coupon coupon = couponRepository.findById(userCoupon.getCouponId())
          .orElseThrow(() -> new PromotionException(PromotionErrorCode.COUPON_NOT_FOUND));
      return CouponResponse.Get.of(coupon);
    });
  }

  @Transactional
  public void updateCoupon(Long couponId, Update request) {
    Coupon coupon = couponRepository.findById(couponId)
        .orElseThrow(() -> new PromotionException(PromotionErrorCode.COUPON_NOT_FOUND));
    coupon.update(request);
  }

  @Transactional
  public void deleteCoupon(Long couponId) {
    Coupon coupon = couponRepository.findById(couponId)
        .orElseThrow(() -> new PromotionException(PromotionErrorCode.COUPON_NOT_FOUND));
    couponRepository.delete(coupon);
  }

  @Transactional
  public void useCoupon(Long couponId, Long userId) {
    UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(userId, couponId);

    if (userCoupon == null) {
      throw new PromotionException(PromotionErrorCode.USER_COUPON_NOT_FOUND);
    }

    if (userCoupon.getIsUsed()) {
      throw new PromotionException(PromotionErrorCode.COUPON_ALREADY_USED);
    }

    userCoupon.updateIsUsed(true);
  }

  @Transactional
  public void refundCoupon(Long couponId, Long userId) {
    UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(userId, couponId);

    if (userCoupon == null) {
      throw new PromotionException(PromotionErrorCode.USER_COUPON_NOT_FOUND);
    }

    if (!userCoupon.getIsUsed()) {
      throw new PromotionException(PromotionErrorCode.COUPON_NOT_USED);
    }

    userCoupon.updateIsUsed(false);
  }

}
