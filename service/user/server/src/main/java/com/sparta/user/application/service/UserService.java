package com.sparta.user.application.service;

import static com.sparta.user.exception.UserErrorCode.USER_CONFLICT;
import static com.sparta.user.exception.UserErrorCode.USER_NOT_FOUND;

import com.sparta.user.domain.model.User;
import com.sparta.user.domain.repository.UserRepository;
import com.sparta.user.dto.infrastructure.UserDto;
import com.sparta.user.exception.UserException;
import com.sparta.user.presentation.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public void createUser(UserRequest.Create request) {
    userRepository
        .findByUsername(request.getUsername())
        .ifPresent(
            user -> {
              throw new UserException(USER_CONFLICT);
            });
    // TODO: 비밀번호 암호화 필요(암호화 시 security 라이브러리가 필요한데 설치하면 테스트 불편함. 이후에 추가) - 경민
    userRepository.save(User.create(request, request.getPassword()));
  }

  public UserDto getUserByUsername(String username) {
    User user = userRepository.findByUsername(username).orElseThrow(
        () -> new UserException(USER_NOT_FOUND));
    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        user.getRole().name()
    );
  }

}
