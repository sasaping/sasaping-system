package com.sparta.user.domain.model.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum UserRole {
  ROLE_ADMIN("관리자"),
  ROLE_MANAGER("매니저"),
  ROLE_USER("사용자");

  private final String role;

  @JsonCreator
  public static UserRole fromString(String role) {
    for (UserRole userRole : UserRole.values()) {
      if (userRole.role.equals(role)) {
        return userRole;
      }
    }
    return null;
  }

  @JsonValue
  public String getRole() {
    return role;
  }
}
