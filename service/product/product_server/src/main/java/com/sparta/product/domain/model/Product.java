package com.sparta.product.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.domain.Persistable;

@Table("P_PRODUCT")
public class Product extends BaseEntity implements Persistable {
  @PrimaryKey private UUID productId = UUID.randomUUID();

  @Column public Long categoryId;

  @Column public String productName;

  @Column public BigDecimal originalPrice;

  @Column public BigDecimal discountedPrice;

  @Column public Double discountPercent;

  @Column public int stock;

  @Column public String description;

  @Column public String thumbnailImgUrl;

  @Column public String detailImgUrl;

  @Column public int limitCountPerUser = 0;
  @Column public double averageRating = 0.0; // TODO :: 리뷰가 등록될떄마다 평균평점 계산
  @Column public boolean isPublic = true;
  @Column public boolean isDeleted = false;
  @Column public boolean isCoupon;
  @Transient private boolean isNew = false;

  @Override
  public Object getId() {
    return null;
  }

  @Transient
  @Override
  public boolean isNew() {
    return this.isNew;
  }

  @Builder
  private Product(
      Long categoryId,
      String productName,
      BigDecimal originalPrice,
      Double discountPercent,
      int stock,
      String description,
      String thumbnailImgUrl,
      String detailImgUrl,
      int limitCountPerUser,
      boolean isCoupon) {
    this.categoryId = categoryId;
    this.productName = productName;
    this.originalPrice = originalPrice;
    this.discountPercent = discountPercent;
    applyDiscount(discountPercent);
    this.stock = stock;
    this.description = description;
    this.thumbnailImgUrl = thumbnailImgUrl;
    this.detailImgUrl = detailImgUrl;
    this.limitCountPerUser = limitCountPerUser;
    this.isCoupon = isCoupon;
  }

  public UUID getProductId() {
    return productId;
  }

  public void setIsNew(boolean isNew) {
    this.isNew = isNew;
  }

  public void applyDiscount(Double discountPercent) {
    if (discountPercent != null) {
      BigDecimal discountPercentBD = BigDecimal.valueOf(discountPercent);
      this.discountedPrice =
          this.originalPrice.multiply(
              BigDecimal.valueOf(1).subtract(discountPercentBD.divide(BigDecimal.valueOf(100))));
    }
  }
}
