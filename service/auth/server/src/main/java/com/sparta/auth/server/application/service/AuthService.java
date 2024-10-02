package com.sparta.auth.server.application.service;

import com.sparta.auth.server.application.dto.AuthResponse;
import com.sparta.auth.server.exception.AuthErrorCode;
import com.sparta.auth.server.exception.AuthException;
import com.sparta.auth.server.presentation.request.AuthRequest;
import com.sparta.user.dto.infrastructure.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

  private final UserService userService;

  public AuthResponse.SignIn signIn(AuthRequest.SignIn request) {
    UserDto userData = userService.getUserByUsername(request.getUsername());

    // TODO(경민): 암호화 된 비밀번호로 비교해야함
    if (userData == null || !userData.getPassword().equals(request.getPassword())) {
      throw new AuthException(AuthErrorCode.SIGN_IN_FAIL);
    }

    // TODO(경민): 토큰 생성 로직 만들어야함. 우선 로그인 기능에만 집중(임시 토큰값 넘김)
    return AuthResponse.SignIn.of(
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwic3ViIjoiMTIzNDU2Nzg5MCIsIm5hbWUiOiJreWVvbmtpbSIsImlhdCI6MTUxNjIzOTAyMn0.oUoAkEIz5XERVqsXw0RFEhvG3JXhbpvEGt9m_P-XBVE");
  }

}
