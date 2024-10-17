package com.sparta.user.presentation.request;

import com.sparta.user.domain.model.vo.UserRole;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Create {

    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "아이디는 4자 이상, 10자 이하의 알파벳 소문자 및 숫자만 허용됩니다.")
    @NotBlank(message = "아이디는 비어 있을 수 없습니다.")
    private String username;

    @Pattern(
        regexp = "^[a-zA-Z0-9_#$%^!-]{8,15}$",
        message = "비밀번호는 8자 이상, 15자 이하의 알파벳 대소문자, 숫자 및 특수문자(_#$%^!-)만 허용됩니다.")
    @NotBlank(message = "비밀번호는 비어 있을 수 없습니다.")
    private String password;

    @Email(message = "이메일 형식이 유효하지 않습니다.")
    @NotBlank(message = "이메일은 비어 있을 수 없습니다.")
    private String email;

    @NotBlank(message = "닉네임은 비어 있을 수 없습니다.")
    private String nickname;

    @DecimalMin(value = "0.0", inclusive = true, message = "포인트는 0 이상이어야 합니다.")
    private BigDecimal point;

    @NotBlank(message = "사용자 타입은 비어 있을 수 없습니다.")
    private UserRole role;

  }

}
