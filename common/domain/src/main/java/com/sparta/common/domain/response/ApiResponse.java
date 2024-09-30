package com.sparta.common.domain.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {
  private String statusName;
  private String message;
  private T data;

  private ApiResponse(String statusName, String message, T data) {
    this.statusName = statusName;
    this.message = message;
    this.data = data;
  }

  public static ApiResponse<Void> error(String statusName, String message) {
    return new ApiResponse<>(statusName, message, null);
  }

  public static <T> ApiResponse<T> error(String statusName, String message, T data) {
    return new ApiResponse<>(statusName, message, data);
  }

  public static <T> ApiResponse<T> created(T data) {
    return new ApiResponse<>("CREATED", null, data);
  }

  public static <T> ApiResponse<T> ok(T data) {
    return new ApiResponse<>("OK", null, data);
  }
}
