<<<<<<<< HEAD:service/auth/dto/src/main/java/com/sparta/auth/dto/jwt/JwtClaim.java
package com.sparta.auth.dto.jwt;
========
package com.sparta.auth.auth_dto.jwt;
>>>>>>>> 90d8e7d67d42838f7c8951951d3ad01da690273c:service/auth/auth_dto/src/main/java/com/sparta/auth/auth_dto/jwt/JwtClaim.java

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class JwtClaim {

  private Long userId;
  private String username;
  private String role;

  public static JwtClaim create(Long userId, String username, String role) {
    return new JwtClaim(userId, username, role);
  }

}
