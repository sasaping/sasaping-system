package com.sparta.product.infrastructure.elasticsearch.dto;

import com.sparta.product.presentation.response.ProductResponse;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@NoArgsConstructor
@Document(indexName = "products")
public class ProductSearchDto {
  @Id private String productId;
  private Long categoryId;
  private String productName;
  private BigDecimal originalPrice;
  private BigDecimal discountedPrice;
  private Double discountPercent;
  private Integer stock;
  private String description;
  private String thumbnailImgUrl;
  private double averageRating;
  private boolean isPublic;
  private boolean isDeleted;
  private boolean isCoupon;

  @Builder
  private ProductSearchDto(
      String productId,
      Long categoryId,
      String productName,
      BigDecimal originalPrice,
      BigDecimal discountedPrice,
      Double discountPercent,
      Integer stock,
      String description,
      String thumbnailImgUrl,
      double averageRating,
      boolean isPublic,
      boolean isDeleted,
      boolean isCoupon) {
    this.productId = productId;
    this.categoryId = categoryId;
    this.productName = productName;
    this.originalPrice = originalPrice;
    this.discountedPrice = discountedPrice;
    this.discountPercent = discountPercent;
    this.stock = stock;
    this.description = description;
    this.thumbnailImgUrl = thumbnailImgUrl;
    this.averageRating = averageRating;
    this.isPublic = isPublic;
    this.isDeleted = isDeleted;
    this.isCoupon = isCoupon;
  }

  public static ProductSearchDto toDto(ProductResponse product) {
    return ProductSearchDto.builder()
        .productId(product.getProductId())
        .categoryId(product.getCategoryId())
        .productName(product.getProductName())
        .originalPrice(product.getOriginalPrice())
        .discountedPrice(product.getDiscountedPrice())
        .discountPercent(product.getDiscountPercent())
        .stock(product.getStock())
        .description(product.getDescription())
        .thumbnailImgUrl(product.getThumbnailImgUrl())
        .averageRating(product.getAverageRating())
        .isPublic(product.isPublic())
        .isDeleted(product.isDeleted())
        .isCoupon(product.isCoupon())
        .build();
  }
}
