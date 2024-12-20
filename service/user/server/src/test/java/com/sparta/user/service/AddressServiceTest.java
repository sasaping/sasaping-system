package com.sparta.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.user.application.dto.AddressResponse;
import com.sparta.user.application.service.AddressService;
import com.sparta.user.domain.model.Address;
import com.sparta.user.domain.model.User;
import com.sparta.user.domain.model.vo.UserRole;
import com.sparta.user.domain.repository.AddressRepository;
import com.sparta.user.domain.repository.UserRepository;
import com.sparta.user.exception.UserErrorCode;
import com.sparta.user.exception.UserException;
import com.sparta.user.presentation.request.AddressRequest;
import com.sparta.user.presentation.request.UserRequest;
import com.sparta.user.user_dto.infrastructure.AddressDto;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AddressServiceTest {

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private AddressRepository addressRepository;

  @Autowired
  private AddressService addressService;

  @Test
  void test_배송지_생성() {
    // given
    UserRequest.Create userRequest = new UserRequest.Create(
        "username", "password", "test@email.com", "nickname", UserRole.ROLE_USER
    );
    User user = User.create(userRequest, "encodedPassword");

    AddressRequest.Create addressRequest = new AddressRequest.Create(
        "집",
        "홍길동",
        "010-1234-5678",
        "12345",
        "서울시 강남구 테헤란로 123",
        false
    );

    // when
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    // then
    addressService.createAddress(user.getId(), addressRequest);
    verify(addressRepository, times(1)).save(any(Address.class));
  }

  @Test
  void test_배송지_생성_사용자_없을_시_예외_발생() {
    // given
    Long userId = 1L;
    AddressRequest.Create addressRequest = new AddressRequest.Create(
        "집",
        "홍길동",
        "010-1234-5678",
        "12345",
        "서울시 강남구 테헤란로 123",
        false
    );

    // when
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // then
    UserException exception = assertThrows(UserException.class, () ->
        addressService.createAddress(userId, addressRequest)
    );

    assertEquals(UserErrorCode.USER_NOT_FOUND.getMessage(), exception.getMessage());
    verify(addressRepository, never()).save(any(Address.class));
  }

  @Test
  void test_배송지_단일_조회_성공() {
    // given
    UserRequest.Create userRequest = new UserRequest.Create(
        "username", "password", "test@email.com", "nickname", UserRole.ROLE_USER
    );
    User user = User.create(userRequest, "encodedPassword");

    Long addressId = 1L;
    Address address = Address.create(user, new AddressRequest.Create(
        "집",
        "홍길동",
        "010-1234-5678",
        "12345",
        "서울시 강남구 테헤란로 123",
        false
    ));

    // when
    when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

    // then
    AddressDto result = addressService.getAddress(addressId);

    assertEquals(user.getId(), result.getUserId());
    assertEquals("집", result.getAlias());
    assertEquals("홍길동", result.getRecipient());
    assertEquals("010-1234-5678", result.getPhoneNumber());
    assertEquals("12345", result.getZipcode());
    assertEquals("서울시 강남구 테헤란로 123", result.getAddress());
    assertEquals(false, result.getIsDefault());
  }

  @Test
  void test_배송지_단일_조회_실패() {
    // given
    Long addressId = 1L;

    when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

    // when & then
    UserException exception = assertThrows(UserException.class, () -> {
      addressService.getAddress(addressId);
    });

    assertEquals(UserErrorCode.ADDRESS_NOT_FOUND.getMessage(), exception.getMessage());
  }

  @Test
  void test_현재_사용자_배송지_조회() {
    // given
    Long userId = 1L;
    UserRequest.Create userRequest = new UserRequest.Create(
        "username", "password", "test@email.com", "nickname", UserRole.ROLE_USER
    );
    User user = User.create(userRequest, "encodedPassword");

    List<Address> addressList = Arrays.asList(
        Address.create(user, new AddressRequest.Create(
            "집",
            "홍길동",
            "010-1234-5678",
            "12345",
            "서울시 강남구 테헤란로 123",
            true
        )),
        Address.create(user, new AddressRequest.Create(
            "집2",
            "홍길동2",
            "010-1234-4321",
            "12347",
            "서울시 강남구 테헤란로 456",
            false
        ))
    );

    // when
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(addressRepository.findAllByUserId(user.getId())).thenReturn(addressList);

    // then
    List<AddressResponse.Get> result = addressService.getAddressByUserId(userId);
    assertEquals(2, result.size());
    verify(userRepository, times(1)).findById(userId);
    verify(addressRepository, times(1)).findAllByUserId(user.getId());
  }

  @Test
  void test_전체_배송지_조회() {
    // given
    UserRequest.Create userRequest = new UserRequest.Create(
        "username", "password", "test@email.com", "nickname", UserRole.ROLE_USER
    );
    User user = User.create(userRequest, "encodedPassword");

    List<Address> addressList = Arrays.asList(
        Address.create(user, new AddressRequest.Create(
            "집",
            "홍길동",
            "010-1234-5678",
            "12345",
            "서울시 강남구 테헤란로 123",
            true
        )),
        Address.create(user, new AddressRequest.Create(
            "집2",
            "홍길동2",
            "010-1234-4321",
            "12347",
            "서울시 강남구 테헤란로 456",
            false
        ))
    );

    // when
    when(addressRepository.findAll()).thenReturn(addressList);

    // then
    List<AddressResponse.Get> result = addressService.getAddressList();
    assertEquals(2, result.size());
    verify(addressRepository, times(1)).findAll();
  }

  @Test
  void test_배송지_수정() {
    // given
    Long userId = 1L;
    Long addressId = 1L;
    UserRequest.Create userRequest = new UserRequest.Create(
        "username", "password", "test@email.com", "nickname", UserRole.ROLE_USER
    );
    User user = User.create(userRequest, "encodedPassword");
    Address address = Address.create(user, new AddressRequest.Create(
        "집",
        "홍길동",
        "010-1234-5678",
        "12345",
        "서울시 강남구 테헤란로 123",
        false
    ));
    AddressRequest.Update addressUpdateRequest = new AddressRequest.Update(
        "회사",
        "김철수",
        "010-9876-5432",
        "54321",
        "서울시 서초구 서초대로 456",
        true
    );

    // when
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(addressRepository.findById(userId)).thenReturn(Optional.of(address));

    // then
    addressService.updateAddress(userId, addressId, addressUpdateRequest);

    assertEquals("회사", address.getAlias());
    assertEquals("김철수", address.getRecipient());
    assertEquals("010-9876-5432", address.getPhoneNumber());
    assertEquals("54321", address.getZipcode());
    assertEquals("서울시 서초구 서초대로 456", address.getAddress());
    assertTrue(address.getIsDefault());

    verify(userRepository, times(1)).findById(userId);
    verify(addressRepository, times(1)).findById(addressId);
  }

  @Test
  void test_배송지_삭제() {
    // given
    Long userId = 1L;
    Long addressId = 1L;

    UserRequest.Create userRequest = new UserRequest.Create(
        "username", "password", "test@email.com", "nickname", UserRole.ROLE_USER
    );
    User user = User.create(userRequest, "encodedPassword");
    Address address = Address.create(user, new AddressRequest.Create(
        "집",
        "홍길동",
        "010-1234-5678",
        "12345",
        "서울시 강남구 테헤란로 123",
        false
    ));

    // when
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

    // delete 주소 실행
    addressService.deleteAddress(userId, addressId);

    // then
    verify(userRepository, times(1)).findById(userId);
    verify(addressRepository, times(1)).findById(addressId);
    verify(addressRepository, times(1)).delete(address);
  }

}
