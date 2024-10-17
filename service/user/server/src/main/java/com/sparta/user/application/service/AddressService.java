package com.sparta.user.application.service;

import static com.sparta.user.exception.UserErrorCode.USER_NOT_FOUND;

import com.sparta.user.application.dto.AddressResponse;
import com.sparta.user.application.dto.AddressResponse.Get;
import com.sparta.user.domain.model.Address;
import com.sparta.user.domain.model.User;
import com.sparta.user.domain.repository.AddressRepository;
import com.sparta.user.domain.repository.UserRepository;
import com.sparta.user.exception.UserErrorCode;
import com.sparta.user.exception.UserException;
import com.sparta.user.presentation.request.AddressRequest;
import com.sparta.user.presentation.request.AddressRequest.Update;
import com.sparta.user.user_dto.infrastructure.AddressDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AddressService {

  private final UserRepository userRepository;
  private final AddressRepository addressRepository;

  @Transactional
  public void createAddress(Long userId, AddressRequest.Create request) {
    User user = userRepository
        .findById(userId)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    addressRepository.save(Address.create(user, request));
  }

  public AddressDto getAddress(Long addressId) {
    Address address = addressRepository
        .findById(addressId)
        .orElseThrow(() -> new UserException(UserErrorCode.ADDRESS_NOT_FOUND));
    return new AddressDto(
        address.getId(),
        address.getUser().getId(),
        address.getAlias(),
        address.getRecipient(),
        address.getPhoneNumber(),
        address.getZipcode(),
        address.getAddress(),
        address.getIsDefault()
    );
  }

  public List<AddressResponse.Get> getAddressByUserId(Long userId) {
    User user = userRepository
        .findById(userId)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    return addressRepository
        .findAllByUserId(user.getId())
        .stream()
        .map(AddressResponse.Get::of)
        .collect(Collectors.toList());
  }

  public List<Get> getAddressList() {
    return addressRepository
        .findAll()
        .stream()
        .map(AddressResponse.Get::of)
        .collect(Collectors.toList());
  }

  @Transactional
  public void updateAddress(Long userId, Long addressId, Update request) {
    userRepository
        .findById(userId)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    Address address = addressRepository
        .findById(addressId)
        .orElseThrow(() -> new UserException(UserErrorCode.ADDRESS_NOT_FOUND));
    address.update(request);
  }

  @Transactional
  public void deleteAddress(Long userId, Long addressId) {
    userRepository
        .findById(userId)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    Address address = addressRepository
        .findById(addressId)
        .orElseThrow(() -> new UserException(UserErrorCode.ADDRESS_NOT_FOUND));
    addressRepository.delete(address);
  }

}
