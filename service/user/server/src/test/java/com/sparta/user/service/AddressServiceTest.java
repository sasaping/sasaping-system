package com.sparta.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.math.BigDecimal;
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
  void 배송지_생성() {
    // given
    UserRequest.Create userRequest = new UserRequest.Create(
        "username", "password", "nickname", BigDecimal.ZERO, UserRole.ROLE_CUSTOMER
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
  void 배송지_생성_사용자없음_예외발생() {
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

}
