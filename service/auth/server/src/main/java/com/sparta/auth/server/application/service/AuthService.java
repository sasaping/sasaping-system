package com.sparta.auth.server.application.service;

import com.sparta.auth.server.application.dto.AuthResponse;
import com.sparta.auth.server.application.dto.JwtClaim;
import com.sparta.auth.server.exception.AuthErrorCode;
import com.sparta.auth.server.exception.AuthException;
import com.sparta.auth.server.infrastructure.utils.JwtHandler;
import com.sparta.auth.server.presentation.request.AuthRequest;
import com.sparta.user.dto.infrastructure.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

  private final UserService userService;
  private final JwtHandler jwtHandler;

  public AuthResponse.SignIn signIn(AuthRequest.SignIn request) {
    UserDto userData = userService.getUserByUsername(request.getUsername());

    // TODO(경민): 암호화 된 비밀번호로 비교해야함
    if (userData == null || !userData.getPassword().equals(request.getPassword())) {
      throw new AuthException(AuthErrorCode.SIGN_IN_FAIL);
    }

    return AuthResponse.SignIn.of(
        jwtHandler.createToken(
            JwtClaim.create(
                userData.getUserId(),
                userData.getUsername(),
                userData.getRole()
            )
        )
    );
  }

}
