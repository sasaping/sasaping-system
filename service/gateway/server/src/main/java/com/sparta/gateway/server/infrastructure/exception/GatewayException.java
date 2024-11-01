package com.sparta.gateway.server.infrastructure.exception;

import com.sparta.common.domain.exception.BusinessException;

public class GatewayException extends BusinessException {

  public GatewayException(GatewayErrorCode errorCode) {
    super(errorCode.getStatus().name(), errorCode.getMessage());
  }
}
