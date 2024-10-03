package com.sparta.product.presentation.exception;

import com.sparta.common.domain.exception.BusinessException;
import lombok.Getter;

@Getter
public class ProductServerException extends BusinessException {
  private ProductErrorCode errorCode;

  public ProductServerException(ProductErrorCode errorCode) {
    super(errorCode.getStatus().name(), errorCode.getMessage());
  }

  public ProductServerException(ProductErrorCode errorCode, Object... args) {
    super(errorCode.getStatus().name(), errorCode.getMessage(), args);
  }
}
