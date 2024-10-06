package com.sparta.auth.server;

import static com.sparta.auth.server.domain.JwtConstant.USER_ID;
import static com.sparta.auth.server.domain.JwtConstant.USER_NAME;
import static com.sparta.auth.server.domain.JwtConstant.USER_ROLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.auth.auth_dto.jwt.JwtClaim;
import com.sparta.auth.server.application.dto.AuthResponse;
import com.sparta.auth.server.application.service.AuthService;
import com.sparta.auth.server.application.service.UserService;
import com.sparta.auth.server.exception.AuthErrorCode;
import com.sparta.auth.server.exception.AuthException;
import com.sparta.auth.server.presentation.request.AuthRequest;
import com.sparta.user.dto.infrastructure.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AuthApplicationTests {

  @MockBean
  private UserService userService;

  @Autowired
  private AuthService authService;

  @Value("${jwt.secret-key}")
  private String secretKeyString;

  private SecretKey secretKey;

  @BeforeEach
  void setUp() {
    // SecretKey 초기화
    secretKey = new SecretKeySpec(secretKeyString.getBytes(StandardCharsets.UTF_8),
        SignatureAlgorithm.HS256.getJcaName());
  }

  @Test
  void test_로그인_성공() {
    // Arrange
    AuthRequest.SignIn request = new AuthRequest.SignIn("testuser", "correctpassword");
    UserDto userDto = new UserDto(1L, "testuser", "correctpassword", "ROLE_ADMIN");

    // Mock the user service to return the userDto when called
    when(userService.getUserByUsername("testuser")).thenReturn(userDto);

    // Act
    AuthResponse.SignIn response = authService.signIn(request);

    // Assert
    assertNotNull(response.getToken());
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

  @Test
  void test_정상_토큰() {
    String token = Jwts.builder()
        .setSubject("test")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 60000)) // 1분 후 만료
        .claim(USER_ID, 1L)
        .claim(USER_NAME, "testUser")
        .claim(USER_ROLE, "ROLE_ADMIN")
        .signWith(secretKey)
        .compact();

    JwtClaim result = authService.verifyToken(token);

    assertEquals(1L, result.getUserId());
    assertEquals("testUser", result.getUsername());
    assertEquals("ROLE_ADMIN", result.getRole());
  }

  @Test
  void test_만료된_토큰() {
    String expiredToken = Jwts.builder()
        .setSubject("test")
        .setIssuedAt(new Date(System.currentTimeMillis() - 2000)) // 2초 전 발급
        .setExpiration(new Date(System.currentTimeMillis() - 1000)) // 1초 전에 만료
        .claim(USER_ID, 1L)
        .claim(USER_NAME, "testUser")
        .claim(USER_ROLE, "ROLE_USER")
        .signWith(secretKey)
        .compact();

    AuthException thrownException = assertThrows(AuthException.class,
        () -> authService.verifyToken(expiredToken));

    assertEquals(
        AuthErrorCode.TOEKN_EXPIRED.getMessage(),
        thrownException.getErrorCode().getMessage());
  }

  @Test
  void test_잘못된_토큰() {
    String invalidToken = Jwts.builder()
        .setSubject("test")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 10000)) // 10초 후 만료
        .claim(USER_ID, 1L)
        .claim(USER_NAME, "testUser")
        .claim(USER_ROLE, "ROLE_USER")
        .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256)) // 다른 키로 서명
        .compact();

    AuthException thrownException = assertThrows(AuthException.class,
        () -> authService.verifyToken(invalidToken));

    assertEquals(AuthErrorCode.INVALID_TOKEN.getMessage(),
        thrownException.getErrorCode().getMessage());
  }

}
