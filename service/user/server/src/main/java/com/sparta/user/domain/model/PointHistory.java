package com.sparta.user.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import com.sparta.user.domain.model.vo.PointHistoryType;
import com.sparta.user.user_dto.infrastructure.PointHistoryDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_point_history")
@Entity
public class PointHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "point_history_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  private Long orderId;

  @Column(nullable = false)
  private BigDecimal point;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PointHistoryType type;

  public static PointHistory create(User user, PointHistoryDto request) {
    return PointHistory.builder()
        .user(user)
        .orderId(request.getOrderId())
        .point(request.getPoint())
        .type(PointHistoryType.from(request.getType()))
        .build();
  }

}
