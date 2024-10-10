package com.sparta.user.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import com.sparta.user.presentation.request.TierRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "p_tier")
@Entity
public class Tier extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tier_id")
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Long thresholdPrice;

  public static Tier create(TierRequest.Create request) {
    return Tier.builder()
        .name(request.getName())
        .thresholdPrice(request.getThresholdPrice())
        .build();
  }

  public void update(TierRequest.Update request) {
    this.name = request.getName();
    this.thresholdPrice = request.getThresholdPrice();
  }

}
