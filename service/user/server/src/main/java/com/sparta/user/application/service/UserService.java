package com.sparta.user.application.service;

import static com.sparta.user.exception.UserErrorCode.USER_NOT_FOUND;

import com.sparta.user.application.dto.UserResponse;
import com.sparta.user.application.dto.UserTierResponse;
import com.sparta.user.domain.model.Tier;
import com.sparta.user.domain.model.User;
import com.sparta.user.domain.model.UserTier;
import com.sparta.user.domain.repository.TierRepository;
import com.sparta.user.domain.repository.UserRepository;
import com.sparta.user.domain.repository.UserTierRepository;
import com.sparta.user.exception.UserErrorCode;
import com.sparta.user.exception.UserException;
import com.sparta.user.presentation.request.UserRequest;
import com.sparta.user.user_dto.infrastructure.UserDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final TierRepository tierRepository;
  private final UserTierRepository userTierRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void createUser(UserRequest.Create request) {
    userRepository
        .findByUsername(request.getUsername())
        .ifPresent(
            user -> {
              throw new UserException(UserErrorCode.USER_CONFLICT);
            });
    Tier defaultTier = tierRepository.findByName("애기핑").orElseThrow(() ->
        new UserException(UserErrorCode.TIER_NOT_FOUND));
    User user = userRepository.save(
        User.create(request, passwordEncoder.encode(request.getPassword())));

    userTierRepository.save(UserTier.create(user, defaultTier));
  }

  public UserDto getUserByUsername(String username) {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        user.getEmail(),
        user.getRole().name(),
        user.getPoint()
    );
  }

  public UserDto getUserByUserId(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        user.getEmail(),
        user.getRole().name(),
        user.getPoint()
    );
  }

  public UserResponse.Info getUserById(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    return UserResponse.Info.of(user);
  }

  public List<UserResponse.Info> getUserList() {
    return userRepository
        .findAllByIsDeletedFalse()
        .stream()
        .map(UserResponse.Info::of)
        .collect(Collectors.toList());
  }

  @Transactional
  public void updateUserPassword(Long userId, UserRequest.UpdatePassword request) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new UserException(UserErrorCode.INVAILD_PASSWORD);
    }
    user.updatePassword(passwordEncoder.encode(request.getUpdatePassword()));
  }

  @Transactional
  public void deleteUser(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    user.delete(true);
  }

  public UserTierResponse.Get getUserTierByUserId(Long userId) {
    UserTier userTier = userTierRepository
        .findByUserId(userId)
        .orElseThrow(() -> new UserException(UserErrorCode.USER_TIER_NOT_FOUND));
    User user = userRepository
        .findById(userTier.getUser().getId())
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    Tier tier = tierRepository
        .findById(userTier.getTier().getId())
        .orElseThrow(() -> new UserException(UserErrorCode.TIER_NOT_FOUND));
    return UserTierResponse.Get.of(user, tier);
  }

  public Page<UserTierResponse.Get> getUserTierList(Pageable pageable) {
    Page<UserTier> userTiers = userTierRepository.findAll(pageable);
    return userTiers.map(userTier -> {
      User user = userRepository
          .findById(userTier.getUser().getId())
          .orElseThrow(() -> new UserException(USER_NOT_FOUND));
      Tier tier = tierRepository
          .findById(userTier.getTier().getId())
          .orElseThrow(() -> new UserException(UserErrorCode.TIER_NOT_FOUND));
      return UserTierResponse.Get.of(user, tier);
    });
  }

  @Transactional
  public void updateUserTier(Long userId, UserRequest.UpdateTier request) {
    User user = userRepository
        .findById(userId)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    UserTier userTier = userTierRepository
        .findByUserId(user.getId())
        .orElseThrow(() -> new UserException(UserErrorCode.USER_TIER_NOT_FOUND));
    Tier tier = tierRepository
        .findByName(request.getTier())
        .orElseThrow(() -> new UserException(UserErrorCode.TIER_NOT_FOUND));
    userTier.update(tier);
  }

}
