package com.sparta.user.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import com.sparta.user.domain.model.vo.UserRole;
import com.sparta.user.presentation.request.UserRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_user")
@Entity
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String nickname;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private BigDecimal point;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private List<Address> addresses;

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private List<PointHistory> pointHistories;

  @Column
  private Boolean isDeleted = false;

  public static User create(UserRequest.Create request, String encodedPassword) {
    return User.builder()
        .username(request.getUsername())
        .password(encodedPassword)
        .nickname(request.getNickname())
        .point(BigDecimal.ZERO)
        .email(request.getEmail())
        .role(request.getRole())
        .build();
  }

  public void updatePoint(BigDecimal point) {
    this.point = point;
  }

  public void updatePassword(String password) {
    this.password = password;
  }

  public void delete(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

}
