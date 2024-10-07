package com.sparta.auth.server.infrastructure.feign;

import com.sparta.auth.server.exception.AuthErrorCode;
import com.sparta.auth.server.exception.AuthException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class UserErrorDecoder implements ErrorDecoder {

  @Override
  public Exception decode(String methodKey, Response response) {
    return new AuthException(AuthErrorCode.INTERNAL_SERVER_ERROR);
  }
}
