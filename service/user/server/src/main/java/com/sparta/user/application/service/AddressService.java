package com.sparta.user.application.service;

import static com.sparta.user.exception.UserErrorCode.USER_NOT_FOUND;

import com.sparta.user.domain.model.Address;
import com.sparta.user.domain.model.User;
import com.sparta.user.domain.repository.AddressRepository;
import com.sparta.user.domain.repository.UserRepository;
import com.sparta.user.exception.UserException;
import com.sparta.user.presentation.request.AddressRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddressService {

  private final UserRepository userRepository;
  private final AddressRepository addressRepository;

  public void createAddress(Long userId, AddressRequest.Create request) {
    User user = userRepository
        .findById(userId)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    addressRepository.save(Address.create(user, request));
  }

}
