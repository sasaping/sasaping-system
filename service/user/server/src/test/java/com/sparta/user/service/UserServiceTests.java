package com.sparta.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.user.application.service.UserService;
import com.sparta.user.domain.model.Tier;
import com.sparta.user.domain.model.User;
import com.sparta.user.domain.model.UserTier;
import com.sparta.user.domain.model.vo.UserRole;
import com.sparta.user.domain.repository.TierRepository;
import com.sparta.user.domain.repository.UserRepository;
import com.sparta.user.domain.repository.UserTierRepository;
import com.sparta.user.exception.UserException;
import com.sparta.user.presentation.request.UserRequest;
import com.sparta.user.user_dto.infrastructure.UserDto;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class UserServiceTests {

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private TierRepository tierRepository;

  @MockBean
  private UserTierRepository userTierRepository;

  @Autowired
  private UserService userService;

  @Test
  void test_회원가입_시_존재하는_유저인지_확인() {
    // Arrange
    UserRequest.Create request =
        new UserRequest.Create("existinguser", "password123", "test@email.com", "nickname",
            BigDecimal.ZERO,
            UserRole.ROLE_ADMIN);

    when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(new User()));

    // Act & Assert
    UserException exception =
        assertThrows(UserException.class, () -> userService.createUser(request));
    assertEquals("이미 존재하는 사용자입니다.", exception.getMessage());

    verify(userRepository, times(1)).findByUsername("existinguser");
    verify(userRepository, never()).save(any());
  }

  @Test
  void test_회원가입() {
    // Arrange
    UserRequest.Create request =
        new UserRequest.Create("newuser", "password123", "test@email.com", "nickname",
            BigDecimal.ZERO,
            UserRole.ROLE_ADMIN);

    when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());

    // when
    User newUser = User.create(request, request.getPassword());
    when(userRepository.save(any(User.class))).thenReturn(newUser);

    Tier defaultTier = new Tier(1L, "애기핑", 50000L);
    when(tierRepository.findByName("애기핑")).thenReturn(Optional.of(defaultTier));

    UserTier userTier = new UserTier(1L, newUser, defaultTier, 0L);
    when(userTierRepository.save(any(UserTier.class))).thenReturn(userTier);
    // Act
    userService.createUser(request);

    // Assert
    verify(userRepository, times(1)).findByUsername("newuser");
    verify(userRepository, times(1)).save(any(User.class));

    // ArgumentCaptor를 사용하여 save 메서드에 전달된 User 객체를 캡처합니다.
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(userCaptor.capture());

    User savedUser = userCaptor.getValue();
    assertEquals("newuser", savedUser.getUsername());
    assertEquals("nickname", savedUser.getNickname());
    assertEquals("test@email.com", savedUser.getEmail());
    assertEquals(BigDecimal.ZERO, savedUser.getPoint());
    assertEquals(UserRole.ROLE_ADMIN, savedUser.getRole());
  }

  @Test
  void test_유저_조회_시_존재하지_않는_유저() {
    // Arrange
    String username = "nonexistentuser";
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Act & Assert
    UserException exception =
        assertThrows(UserException.class, () -> userService.getUserByUsername(username));
    assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());

    verify(userRepository, times(1)).findByUsername(username);
  }

  @Test
  void test_유저_조회_성공() {
    // Arrange
    String username = "existinguser";
    UserRequest.Create request =
        new UserRequest.Create(username, "password123", "nickname", "test@email.com",
            BigDecimal.ZERO,
            UserRole.ROLE_ADMIN);

    User existingUser = User.create(request, "password123");

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

    // Act
    UserDto userDto = userService.getUserByUsername(username);

    // Assert
    assertEquals(username, userDto.getUsername());
    assertEquals("password123", userDto.getPassword());
    assertEquals(UserRole.ROLE_ADMIN.name(), userDto.getRole());

    verify(userRepository, times(1)).findByUsername(username);
  }

}
