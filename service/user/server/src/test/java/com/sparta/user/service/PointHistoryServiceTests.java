package com.sparta.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.user.application.service.PointHistoryService;
import com.sparta.user.domain.model.PointHistory;
import com.sparta.user.domain.model.User;
import com.sparta.user.domain.model.vo.UserRole;
import com.sparta.user.domain.repository.PointHistoryRepository;
import com.sparta.user.domain.repository.UserRepository;
import com.sparta.user.exception.UserErrorCode;
import com.sparta.user.exception.UserException;
import com.sparta.user.presentation.request.UserRequest;
import com.sparta.user.user_dto.infrastructure.PointHistoryDto;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class PointHistoryServiceTests {

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private PointHistoryRepository pointHistoryRepository;

  @Autowired
  private PointHistoryService pointHistoryService;

  @Test
  void test_포인트_내역_추가_성공() {
    // Given
    UserRequest.Create userRequest = new UserRequest.Create(
        "username", "password", "nickname", BigDecimal.ZERO, UserRole.ROLE_CUSTOMER
    );
    User user = User.create(userRequest, "encodedPassword");
    PointHistoryDto request = new PointHistoryDto(
        user.getId(),
        10L,
        new BigDecimal("50.0"),
        "적립",
        "포인트 적립"
    );

    // Mock user repository
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    // When
    pointHistoryService.createPointHistory(request);

    // Then
    verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
  }

  @Test
  void test_포인트_내역_추가_시_존재하지_않는_유저() {
    // Given
    Long nonExistentUserId = 999L; // 존재하지 않는 유저 ID
    PointHistoryDto request = new PointHistoryDto(
        nonExistentUserId,
        10L,
        new BigDecimal("50.0"),
        "적립",
        "포인트 적립"
    );

    // Mock user repository to return empty for non-existent user
    when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

    // When & Then
    UserException exception = assertThrows(UserException.class, () -> {
      pointHistoryService.createPointHistory(request);
    });

    assertEquals(UserErrorCode.USER_NOT_FOUND.getMessage(), exception.getMessage());
  }

  @Test
  void test_포인트_내역_추가_시_유저의_포인트_부족() {
    // Given
    UserRequest.Create userRequest = new UserRequest.Create(
        "username", "password", "nickname", new BigDecimal("100"), UserRole.ROLE_CUSTOMER
    );
    User user = User.create(userRequest, "encodedPassword");
    PointHistoryDto request = new PointHistoryDto(
        user.getId(),
        10L,
        new BigDecimal("200"), // 사용하려는 포인트가 유저 포인트보다 큼
        "사용",
        "포인트 사용"
    );

    // Mock user repository
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    // When & Then
    UserException exception = assertThrows(UserException.class, () -> {
      pointHistoryService.createPointHistory(request);
    });

    assertEquals(UserErrorCode.INSUFFICIENT_POINTS.getMessage(), exception.getMessage());
  }

}
