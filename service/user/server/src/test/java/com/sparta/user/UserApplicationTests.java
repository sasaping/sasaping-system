package com.sparta.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.user.application.service.UserService;
import com.sparta.user.domain.model.User;
import com.sparta.user.domain.model.vo.UserRole;
import com.sparta.user.domain.repository.UserRepository;
import com.sparta.user.exception.UserException;
import com.sparta.user.presentation.request.UserRequest;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class UserApplicationTests {

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @Test
  void testSignUp_UserAlreadyExists_ThrowsUserException() {
    // Arrange
    UserRequest.Create request = new UserRequest.Create(
        "existinguser",
        "password123",
        "nickname",
        0,
        UserRole.ROLE_ADMIN
    );

    when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(new User()));

    // Act & Assert
    UserException exception = assertThrows(UserException.class,
        () -> userService.createUser(request));
    assertEquals("이미 존재하는 사용자입니다.", exception.getMessage());

    verify(userRepository, times(1)).findByUsername("existinguser");
    verify(userRepository, never()).save(any());
  }

  @Test
  void testSignUp_NewUser_SavesUser() {
    // Arrange
    UserRequest.Create request = new UserRequest.Create(
        "newuser",
        "password123",
        "nickname",
        0,
        UserRole.ROLE_ADMIN
    );

    when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());

    // 새 User 객체를 생성하고 save 메서드가 반환하도록 설정합니다.
    User newUser = User.create(request, request.getPassword());
    when(userRepository.save(any(User.class))).thenReturn(newUser);

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
    assertEquals(0, savedUser.getPoint());
    assertEquals(UserRole.ROLE_ADMIN, savedUser.getRole());
  }

}