package com.sparta.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.user.application.dto.UserResponse;
import com.sparta.user.application.dto.UserTierResponse;
import com.sparta.user.application.service.UserService;
import com.sparta.user.domain.model.Tier;
import com.sparta.user.domain.model.User;
import com.sparta.user.domain.model.UserTier;
import com.sparta.user.domain.model.vo.UserRole;
import com.sparta.user.domain.repository.TierRepository;
import com.sparta.user.domain.repository.UserRepository;
import com.sparta.user.domain.repository.UserTierRepository;
import com.sparta.user.exception.UserErrorCode;
import com.sparta.user.exception.UserException;
import com.sparta.user.presentation.request.UserRequest;
import com.sparta.user.user_dto.infrastructure.UserDto;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class UserServiceTests {

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private TierRepository tierRepository;

  @MockBean
  private UserTierRepository userTierRepository;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserService userService;

  @Test
  void test_회원가입_실패_존재하는_유저() {
    // Arrange
    UserRequest.Create request =
        new UserRequest.Create("existinguser", "password123", "test@email.com", "nickname",
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
  void test_회원가입_성공() {
    // Arrange
    UserRequest.Create request =
        new UserRequest.Create("newuser", "password123", "test@email.com", "nickname",
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
  void test_유저_조회_실패_존재하지_않는_유저() {
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

  @Test
  void test_단일_유저_조회_성공() {
    // Arrange
    Long userId = 1L;
    UserRequest.Create request =
        new UserRequest.Create("testUser", "password123", "nickname", "test@email.com",
            UserRole.ROLE_ADMIN);

    User newUser = User.create(request, "password123");

    when(userRepository.findById(userId)).thenReturn(Optional.of(newUser));

    // Act
    UserResponse.Info result = userService.getUserById(userId);

    // Assert
    assertEquals("testUser", result.getUsername());
    assertEquals(UserRole.ROLE_ADMIN.name(), result.getRole());
    assertEquals(BigDecimal.ZERO, result.getPoint());

    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  void test_단일_유저_조회_실패() {
    // Arrange
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // Act & Assert
    UserException exception = assertThrows(UserException.class,
        () -> userService.getUserById(userId));
    assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());

    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  void test_전체_사용자_조회() {
    // given
    UserRequest.Create userRequest1 = new UserRequest.Create(
        "username1", "password", "test1@test.com", "nickname1", UserRole.ROLE_USER
    );
    UserRequest.Create userRequest2 = new UserRequest.Create(
        "username2", "password", "test2@test.com", "nickname2", UserRole.ROLE_ADMIN
    );

    List<User> userList = Arrays.asList(
        User.create(userRequest1, "password"),
        User.create(userRequest2, "password")
    );

    // when
    when(userRepository.findAllByIsDeletedFalse()).thenReturn(userList);

    // then
    List<UserResponse.Info> result = userService.getUserList();
    assertEquals(2, result.size());
    verify(userRepository, times(1)).findAllByIsDeletedFalse();
  }

  @Test
  void test_비밀번호_업데이트_성공() {
    // given
    Long userId = 1L;
    UserRequest.Create request = new UserRequest.Create(
        "username1", "password", "test@test.com", "nickname1", UserRole.ROLE_USER
    );
    User newUser = User.create(request, "password123");
    UserRequest.UpdatePassword updatePassword = new UserRequest.UpdatePassword(
        "password123", "password"
    );

    // when
    when(userRepository.findById(userId)).thenReturn(Optional.of(newUser));
    when(passwordEncoder.matches(updatePassword.getCurrentPassword(),
        newUser.getPassword())).thenReturn(true);
    when(passwordEncoder.encode(updatePassword.getUpdatePassword())).thenReturn("password456");

    // then
    userService.updateUserPassword(userId, updatePassword);

    assertEquals("password456", newUser.getPassword());
  }

  @Test
  void test_비밀번호_업데이트_실패_잘못된_현재_비밀번호() {
    // given
    Long userId = 1L;
    UserRequest.Create request = new UserRequest.Create(
        "username1", "password", "test@test.com", "nickname1", UserRole.ROLE_USER
    );
    User newUser = User.create(request, "password123");
    UserRequest.UpdatePassword updatePassword = new UserRequest.UpdatePassword(
        "wrongCurrentPassword", "password"
    );

    // when
    when(userRepository.findById(userId)).thenReturn(Optional.of(newUser));
    when(passwordEncoder.matches(updatePassword.getCurrentPassword(),
        newUser.getPassword())).thenReturn(false);

    // then
    UserException exception = assertThrows(UserException.class,
        () -> userService.updateUserPassword(userId, updatePassword));
    assertEquals(UserErrorCode.INVAILD_PASSWORD.getMessage(), exception.getMessage());
  }

  @Test
  void test_유저_삭제_성공() {
    // given
    Long userId = 1L;
    User user = new User();
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    // when & then
    userService.deleteUser(userId);

    // verify
    verify(userRepository).findById(userId);
  }

  @Test
  void test_유저_삭제_실패_유저없음() {
    // given
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // when & then
    UserException exception = assertThrows(UserException.class,
        () -> userService.deleteUser(userId));

    assertEquals(UserErrorCode.USER_NOT_FOUND, exception.getErrorCode());

    // verify
    verify(userRepository).findById(userId);
  }

  @Test
  void test_유저_티어_단일_조회_성공() {
    // given
    Long userId = 1L;
    Long tierId = 1L;

    UserRequest.Create requestUser = new UserRequest.Create(
        "testUser", "password", "test@test.com", "nickname1", UserRole.ROLE_USER
    );
    User user = User.create(requestUser, "password123");
    Tier tier = new Tier(tierId, "실버핑", 100000L);
    UserTier userTier = UserTier.create(user, tier);

    // when
    when(userTierRepository.findByUserId(userId)).thenReturn(Optional.of(userTier));
    when(userRepository.findById(userTier.getUser().getId())).thenReturn(Optional.of(user));
    when(tierRepository.findById(userTier.getTier().getId())).thenReturn(Optional.of(tier));

    // then
    UserTierResponse.Get response = userService.getUserTierByUserId(userId);

    // assert
    assertEquals("testUser", response.getUsername());
    assertEquals("실버핑", response.getTier());

    verify(userTierRepository, times(1)).findByUserId(userId);
    verify(userRepository, times(1)).findById(userTier.getUser().getId());
    verify(tierRepository, times(1)).findById(userTier.getTier().getId());
  }

  @Test
  void test_유저_티어_단일_조회_실패_유저티어_없음() {
    // given
    Long userId = 1L;

    // when
    when(userTierRepository.findByUserId(userId)).thenReturn(Optional.empty());

    // then
    UserException exception = assertThrows(UserException.class,
        () -> userService.getUserTierByUserId(userId));

    // assert
    assertEquals(UserErrorCode.USER_TIER_NOT_FOUND, exception.getErrorCode());

    verify(userTierRepository, times(1)).findByUserId(userId);
    verify(userRepository, never()).findById(anyLong());
    verify(tierRepository, never()).findById(anyLong());
  }

  @Test
  void test_유저_티어_단일_조회_실패_유저_없음() {
    // given
    Long userId = 1L;

    User user = mock(User.class);
    Tier tier = mock(Tier.class);
    UserTier userTier = UserTier.create(user, tier);

    // when
    when(userTierRepository.findByUserId(userId)).thenReturn(Optional.of(userTier));
    when(userRepository.findById(userTier.getUser().getId())).thenReturn(Optional.empty());

    // then
    UserException exception = assertThrows(UserException.class,
        () -> userService.getUserTierByUserId(userId));

    // assert
    assertEquals(UserErrorCode.USER_NOT_FOUND, exception.getErrorCode());

    verify(userTierRepository, times(1)).findByUserId(userId);
    verify(userRepository, times(1)).findById(userTier.getUser().getId());
    verify(tierRepository, never()).findById(anyLong());
  }

  @Test
  void test_유저_티어_단일_조회_실패_티어_없음() {
    // given
    Long userId = 1L;

    User user = mock(User.class);
    Tier tier = mock(Tier.class);
    UserTier userTier = UserTier.create(user, tier);

    // when
    when(userTierRepository.findByUserId(userId)).thenReturn(Optional.of(userTier));
    when(userRepository.findById(userTier.getUser().getId())).thenReturn(Optional.of(user));
    when(tierRepository.findById(userTier.getTier().getId())).thenReturn(Optional.empty());

    // then
    UserException exception = assertThrows(UserException.class,
        () -> userService.getUserTierByUserId(userId));

    // assert
    assertEquals(UserErrorCode.TIER_NOT_FOUND, exception.getErrorCode());

    verify(userTierRepository, times(1)).findByUserId(userId);
    verify(userRepository, times(1)).findById(userTier.getUser().getId());
    verify(tierRepository, times(1)).findById(userTier.getTier().getId());
  }

  @Test
  void test_유저_티어_전체_조회_성공() {
    // given
    Pageable pageable = PageRequest.of(0, 10);

    User user = mock(User.class);
    Tier tier = mock(Tier.class);
    UserTier userTier1 = UserTier.create(user, tier);
    UserTier userTier2 = UserTier.create(user, tier);

    List<UserTier> userTierList = Arrays.asList(userTier1, userTier2);
    Page<UserTier> userTierPage = new PageImpl<>(userTierList, pageable, userTierList.size());

    // when
    when(userTierRepository.findAll(pageable)).thenReturn(userTierPage);
    when(userRepository.findById(userTier1.getUser().getId())).thenReturn(Optional.of(user));
    when(userRepository.findById(userTier2.getUser().getId())).thenReturn(Optional.of(user));
    when(tierRepository.findById(userTier1.getTier().getId())).thenReturn(Optional.of(tier));
    when(tierRepository.findById(userTier2.getTier().getId())).thenReturn(Optional.of(tier));

    // then
    Page<UserTierResponse.Get> response = userService.getUserTierList(pageable);

    // assert
    assertEquals(2, response.getTotalElements());

    verify(userTierRepository, times(1)).findAll(pageable);
    verify(userRepository, times(2)).findById(anyLong());
    verify(tierRepository, times(2)).findById(anyLong());
  }

  @Test
  void test_유저_티어_업데이트_성공() {
    // given
    Long userId = 1L;
    Long tierId = 1L;

    User user = mock(User.class);
    Tier oldTier = new Tier(tierId, "애기핑", 50000L);

    UserTier userTier = UserTier.create(user, oldTier);

    Tier newTier = new Tier(tierId, "실버핑", 100000L);
    UserRequest.UpdateTier request = new UserRequest.UpdateTier("실버핑");

    // when
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userTierRepository.findByUserId(user.getId())).thenReturn(Optional.of(userTier));
    when(tierRepository.findByName("실버핑")).thenReturn(Optional.of(newTier));

    // then
    userService.updateUserTier(userId, request);

    // assert
    assertEquals("실버핑", userTier.getTier().getName());
  }

  @Test
  void test_유저_티어_업데이트_실패_유저없음() {
    // given
    Long userId = 1L;
    UserRequest.UpdateTier request = new UserRequest.UpdateTier("실버핑");

    // when
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // then
    UserException exception = assertThrows(UserException.class,
        () -> userService.updateUserTier(userId, request));

    // assert
    assertEquals(UserErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void test_유저_티어_업데이트_실패_티어없음() {
    // given
    Long userId = 1L;
    User user = mock(User.class);
    Tier tier = mock(Tier.class);

    UserTier userTier = UserTier.create(user, tier);

    UserRequest.UpdateTier request = new UserRequest.UpdateTier("실버핑");

    // when
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userTierRepository.findByUserId(user.getId())).thenReturn(Optional.of(userTier));
    when(tierRepository.findByName("실버핑")).thenReturn(Optional.empty());

    // then
    UserException exception = assertThrows(UserException.class,
        () -> userService.updateUserTier(userId, request));

    // assert
    assertEquals(UserErrorCode.TIER_NOT_FOUND, exception.getErrorCode());
  }

}
