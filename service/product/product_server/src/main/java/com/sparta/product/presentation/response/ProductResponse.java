package com.sparta.product.presentation.response;

import com.sparta.product.domain.model.Product;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponse {
  private String productId;
  private Long categoryId;
  private String productName;
  private BigDecimal originalPrice;
  private BigDecimal discountedPrice;
  private Double discountPercent;
  private Integer stock;
  private String description;
  private String thumbnailImgUrl;
  private String detailImgUrl;
  private int limitCountPerUser;
  private double averageRating;
  private boolean isPublic;
  private boolean soldout;
  private boolean isDeleted;
  private boolean isCoupon;

  @Builder
  private ProductResponse(
      UUID productId,
      Long categoryId,
      String productName,
      BigDecimal originalPrice,
      BigDecimal discountedPrice,
      Double discountPercent,
      int stock,
      String description,
      String thumbnailImgUrl,
      String detailImgUrl,
      int limitCountPerUser,
      double averageRating,
      boolean isPublic,
      boolean soldout,
      boolean isDeleted,
      boolean isCoupon) {
    this.productId = productId.toString();
    this.categoryId = categoryId;
    this.productName = productName;
    this.originalPrice = originalPrice;
    this.discountedPrice = discountedPrice;
    this.discountPercent = discountPercent;
    this.stock = stock;
    this.description = description;
    this.thumbnailImgUrl = thumbnailImgUrl;
    this.detailImgUrl = detailImgUrl;
    this.limitCountPerUser = limitCountPerUser;
    this.averageRating = averageRating;
    this.isPublic = isPublic;
    this.soldout = soldout;
    this.isDeleted = isDeleted;
    this.isCoupon = isCoupon;
  }

  public static ProductResponse fromEntity(Product product) {
    return ProductResponse.builder()
        .productId(product.getProductId())
        .categoryId(product.categoryId)
        .productName(product.productName)
        .description(product.description)
        .thumbnailImgUrl(product.thumbnailImgUrl)
        .detailImgUrl(product.detailImgUrl)
        .originalPrice(product.originalPrice)
        .discountPercent(product.discountPercent)
        .discountedPrice(product.discountedPrice)
        .stock(product.stock)
        .limitCountPerUser(product.limitCountPerUser)
        .averageRating(product.averageRating)
        .isCoupon(product.isCoupon)
        .isDeleted(product.isDeleted)
        .isPublic(product.isPublic)
        .soldout(product.soldout)
        .build();
  }
}
