package com.sparta.auth.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.auth.server.application.dto.AuthResponse;
import com.sparta.auth.server.application.dto.JwtClaim;
import com.sparta.auth.server.application.service.AuthService;
import com.sparta.auth.server.application.service.UserService;
import com.sparta.auth.server.exception.AuthException;
import com.sparta.auth.server.infrastructure.utils.JwtHandler;
import com.sparta.auth.server.presentation.request.AuthRequest;
import com.sparta.user.dto.infrastructure.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AuthApplicationTests {

  @MockBean
  private UserService userService;

  @MockBean
  private JwtHandler jwtHandler;

  @Autowired
  private AuthService authService;

  @Test
  void test_로그인_성공() {
    // Arrange
    AuthRequest.SignIn request = new AuthRequest.SignIn("testuser", "correctpassword");
    UserDto userDto = new UserDto(1L, "testuser", "correctpassword", "ROLE_ADMIN");

    // Mock the user service to return the userDto when called
    when(userService.getUserByUsername("testuser")).thenReturn(userDto);

    // Mock the jwtHandler to return a specific token when createToken is called
    String expectedToken = "mockedToken123";
    when(jwtHandler.createToken(any(JwtClaim.class))).thenReturn(expectedToken);

    // Act
    AuthResponse.SignIn response = authService.signIn(request);

    // Assert
    assertEquals(expectedToken, response.getToken());

    verify(userService, times(1)).getUserByUsername("testuser");
    verify(jwtHandler, times(1)).createToken(any(JwtClaim.class));
  }

  @Test
  void test_로그인_실패_비밀번호_불일치() {
    // Arrange
    AuthRequest.SignIn request = new AuthRequest.SignIn("testuser", "wrongpassword");
    UserDto userDto = new UserDto(1L, "testuser", "correctpassword", "ROLE_USER");
    when(userService.getUserByUsername("testuser")).thenReturn(userDto);

    // Act & Assert
    AuthException exception = assertThrows(AuthException.class,
        () -> authService.signIn(request));
    assertEquals("로그인 실패", exception.getMessage());

    verify(userService, times(1)).getUserByUsername("testuser");
  }

  @Test
  void test_로그인_실패_존재하지_않는_유저() {
    // Arrange
    AuthRequest.SignIn request = new AuthRequest.SignIn("nonexistentuser", "password123");
    when(userService.getUserByUsername("nonexistentuser")).thenReturn(null);

    // Act & Assert
    AuthException exception = assertThrows(AuthException.class,
        () -> authService.signIn(request));
    assertEquals("로그인 실패", exception.getMessage());

    verify(userService, times(1)).getUserByUsername("nonexistentuser");
  }

}
