package com.sparta.product.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "P_PREORDER")
@NoArgsConstructor
@Getter
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE p_preorder SET is_deleted = true where preorder_id = ?")
public class PreOrder extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "preorder_id")
  private Long preOrderId;

  @Column(nullable = false)
  private UUID productId;

  @Column(nullable = false)
  private String preOrderTitle;

  @Column(nullable = false)
  private LocalDateTime startDateTime;

  @Column(nullable = false)
  private LocalDateTime endDateTime;

  @Column(nullable = false)
  private LocalDateTime releaseDateTime;

  @Column
  @Enumerated(EnumType.STRING)
  private PreOrderState state = PreOrderState.INITIALIZED;

  @Column(nullable = false)
  private Integer availableQuantity;

  @Column private boolean isPublic = true;

  @Column private boolean isDeleted = false;

  @Builder
  private PreOrder(
      UUID productId,
      String preOrderTitle,
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      LocalDateTime releaseDateTime,
      Integer availableQuantity) {
    this.productId = productId;
    this.preOrderTitle = preOrderTitle;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.releaseDateTime = releaseDateTime;
    this.availableQuantity = availableQuantity;
  }

  public void open() {
    this.state = PreOrderState.OPEN_FOR_ORDER;
  }

  public void cancel() {
    this.state = PreOrderState.CANCELLED;
    this.isPublic = false;
  }

  public void update(
      UUID productId,
      String preOrderTitle,
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      LocalDateTime releaseDateTime,
      Integer availableQuantity) {
    this.productId = productId;
    this.preOrderTitle = preOrderTitle;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.releaseDateTime = releaseDateTime;
    this.availableQuantity = availableQuantity;
  }
}
