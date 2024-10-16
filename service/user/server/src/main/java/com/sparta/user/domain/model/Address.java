package com.sparta.user.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import com.sparta.user.presentation.request.AddressRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_address")
@Entity
public class Address extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "address_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private String alias;

  @Column(nullable = false)
  private String recipient;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String zipcode;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private Boolean isDefault;

  public static Address create(User user, AddressRequest.Create request) {
    return Address.builder()
        .user(user)
        .alias(request.getAlias())
        .recipient(request.getRecipient())
        .phoneNumber(request.getPhoneNumber())
        .zipcode(request.getZipcode())
        .address(request.getAddress())
        .isDefault(request.getIsDefault())
        .build();
  }

  public void update(AddressRequest.Update request) {
    this.alias = request.getAlias();
    this.recipient = request.getRecipient();
    this.phoneNumber = request.getPhoneNumber();
    this.zipcode = request.getZipcode();
    this.address = request.getAddress();
    this.isDefault = request.getIsDefault();
  }

}
