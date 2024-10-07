package com.sparta.auth.server.application.service;

import com.sparta.user.user_dto.infrastructure.UserDto;

public interface UserService {

  UserDto getUserByUsername(String username);

}
