package com.sparta.promotion.server.exception;

import com.sparta.common.domain.exception.BusinessException;
import lombok.Getter;

@Getter
public class PromotionException extends BusinessException {

  PromotionErrorCode errorCode;

  public PromotionException(PromotionErrorCode errorCode) {
    super(errorCode.getStatus().name(), errorCode.getMessage());
    this.errorCode = errorCode;
  }

}
