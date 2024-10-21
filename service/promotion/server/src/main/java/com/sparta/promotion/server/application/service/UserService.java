package com.sparta.promotion.server.application.service;

import com.sparta.user.user_dto.infrastructure.UserDto;

public interface UserService {

  UserDto getUserByUserId(Long userId);

}
