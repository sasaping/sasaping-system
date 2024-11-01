package com.sparta.user.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AddressRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Create {

    @NotNull
    private String alias;
    @NotNull
    private String recipient;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String zipcode;
    @NotNull
    private String address;
    @NotNull
    private Boolean isDefault;

  }
  
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Update {

    @NotNull
    private String alias;
    @NotNull
    private String recipient;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String zipcode;
    @NotNull
    private String address;
    @NotNull
    private Boolean isDefault;

  }

}
