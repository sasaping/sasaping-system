package com.sparta.user.application.service;

import static com.sparta.user.exception.UserErrorCode.USER_CONFLICT;
import static com.sparta.user.exception.UserErrorCode.USER_NOT_FOUND;

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
import lombok.RequiredArgsConstructor;
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
              throw new UserException(USER_CONFLICT);
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
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    return new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getRole().name());
  }

}
