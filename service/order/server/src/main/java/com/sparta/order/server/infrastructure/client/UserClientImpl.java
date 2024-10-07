package com.sparta.order.server.infrastructure.client;

import com.sparta.user.user_dto.infrastructure.UserInternalDto.UserOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

// 임시 구현체
@Slf4j
@Service
public class UserClientImpl implements UserClient {


  @Override
  public UserOrderResponse getUser(Long userId) {
    return new UserOrderResponse(1L, "사용자", "고객", 10000);
  }

  @Override
  public void usePoint(int point) {
    log.info("포인트 사용 완료");
  }

  @Override
  public String getAddress(Long addressId) {
    log.info("배송지 조회 완료");
    return "배송지";
  }

}
