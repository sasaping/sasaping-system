package com.sparta.product.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.domain.Persistable;

@Table("P_PRODUCT")
@Getter
public class Product extends BaseEntity implements Persistable {
  @PrimaryKey private UUID productId = UUID.randomUUID();
  @Column private Long categoryId;
  @Column private String productName;
  @Column private String brandName;
  @Column private String mainColor;
  @Column private String size;
  @Column private String description;

  @Column private BigDecimal originalPrice;
  @Column private BigDecimal discountedPrice;
  @Column private Double discountPercent;
  @Column private int stock;

  @Column private String originImgUrl;
  @Column private String thumbnailImgUrl;
  @Column private String detailImgUrl;

  @Column private int limitCountPerUser = 0;
  @Column private double averageRating = 0.0;
  @Column private long reviewCount = 0;
  @Column private long salesCount = 0;

  @Column private boolean isPublic = true;
  @Column private boolean soldout = false;
  @Column private boolean isDeleted = false;
  @Column private List<String> tags;
  @Transient private boolean isNew = false;

  @Override
  public Object getId() {
    return this.productId;
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
      String brandName,
      String mainColor,
      String size,
      BigDecimal originalPrice,
      Double discountPercent,
      int stock,
      String description,
      String originImgUrl,
      String thumbnailImgUrl,
      String detailImgUrl,
      int limitCountPerUser,
      List<String> tags) {
    this.categoryId = categoryId;
    this.productName = productName;
    this.brandName = brandName;
    this.mainColor = mainColor;
    this.size = size;
    this.originalPrice = originalPrice;
    this.discountPercent = discountPercent;
    applyDiscount(discountPercent);
    this.stock = stock;
    this.description = description;
    this.originImgUrl = originImgUrl;
    this.thumbnailImgUrl = thumbnailImgUrl;
    this.detailImgUrl = detailImgUrl;
    this.limitCountPerUser = limitCountPerUser;
    this.tags = tags;
  }

  public void updateProduct(
      Long categoryId,
      String productName,
      String brandName,
      String mainColor,
      String size,
      BigDecimal originalPrice,
      Double discountPercent,
      Integer stock,
      String description,
      String originImgUrl,
      String detailImgUrl,
      String thumbnailImgUrl,
      Integer limitCountPerUser,
      List<String> tags,
      boolean isPublic) {
    this.categoryId = categoryId;
    this.productName = productName;
    this.brandName = brandName;
    this.mainColor = mainColor;
    this.size = size;
    this.originalPrice = originalPrice;
    this.discountPercent = discountPercent;
    applyDiscount(discountPercent);
    this.stock = stock;
    this.description = description;
    this.originImgUrl = originImgUrl;
    this.detailImgUrl = detailImgUrl;
    this.thumbnailImgUrl = thumbnailImgUrl;
    this.tags = tags;
    this.limitCountPerUser = limitCountPerUser;
    this.isPublic = isPublic;
  }

  public UUID getProductId() {
    return productId;
  }

  public void setIsNew(boolean isNew) {
    this.isNew = isNew;
  }

  public void isDelete() {
    this.isDeleted = true;
  }

  public void setSoldout(boolean status) {
    this.soldout = status;
  }

  public void applyDiscount(Double discountPercent) {
    if (discountPercent != null) {
      BigDecimal discountPercentBD = BigDecimal.valueOf(discountPercent);
      this.discountedPrice =
          this.originalPrice.multiply(
              BigDecimal.valueOf(1).subtract(discountPercentBD.divide(BigDecimal.valueOf(100))));
    } else {
      this.discountedPrice = this.originalPrice;
    }
  }

  public void updateStock(int reduceCount) {
    this.stock -= reduceCount;
  }

  public void rollbackStock(int rollbackCount) {
    this.stock += rollbackCount;
  }
}
