package com.sparta.user.user_dto.infrastructure;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class AddressDto {

  private Long addressId;
  private Long userId;
  private String alias;
  private String recipient;
  private String phoneNumber;
  private String zipcode;
  private String address;
  private Boolean isDefault;

}
