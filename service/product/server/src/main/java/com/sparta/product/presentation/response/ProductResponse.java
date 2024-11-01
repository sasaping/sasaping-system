package com.sparta.product.presentation.response;

import com.sparta.product.domain.model.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponse {
  private String productId;
  private Long categoryId;
  private String brandName;
  private String mainColor;
  private String size;
  private String productName;
  private BigDecimal originalPrice;
  private BigDecimal discountedPrice;
  private Double discountPercent;
  private Integer stock;
  private String description;
  private String originImgUrl;
  private String thumbnailImgUrl;
  private String detailImgUrl;
  private int limitCountPerUser;
  private double averageRating;
  private long reviewCount;
  private long salesCount;
  private boolean isPublic;
  private boolean soldout;
  private boolean isDeleted;
  private List<String> tags;
  private LocalDateTime createdAt;

  @Builder
  private ProductResponse(
      UUID productId,
      Long categoryId,
      String productName,
      String brandName,
      String mainColor,
      String size,
      BigDecimal originalPrice,
      BigDecimal discountedPrice,
      Double discountPercent,
      int stock,
      String description,
      String originImgUrl,
      String thumbnailImgUrl,
      String detailImgUrl,
      int limitCountPerUser,
      double averageRating,
      long reviewCount,
      long salesCount,
      boolean isPublic,
      boolean soldout,
      boolean isDeleted,
      List<String> tags,
      LocalDateTime createdAt) {
    this.productId = productId.toString();
    this.categoryId = categoryId;
    this.productName = productName;
    this.brandName = brandName;
    this.mainColor = mainColor;
    this.size = size;
    this.originalPrice = originalPrice;
    this.discountedPrice = discountedPrice;
    this.discountPercent = discountPercent;
    this.stock = stock;
    this.description = description;
    this.originImgUrl = originImgUrl;
    this.thumbnailImgUrl = thumbnailImgUrl;
    this.detailImgUrl = detailImgUrl;
    this.limitCountPerUser = limitCountPerUser;
    this.averageRating = averageRating;
    this.reviewCount = reviewCount;
    this.salesCount = salesCount;
    this.isPublic = isPublic;
    this.soldout = soldout;
    this.isDeleted = isDeleted;
    this.tags = tags;
    this.createdAt = createdAt;
  }

  public static ProductResponse fromEntity(Product product) {
    return ProductResponse.builder()
        .productId(product.getProductId())
        .categoryId(product.getCategoryId())
        .productName(product.getProductName())
        .brandName(product.getBrandName())
        .mainColor(product.getMainColor())
        .size(product.getSize())
        .description(product.getDescription())
        .originImgUrl(product.getOriginImgUrl())
        .thumbnailImgUrl(product.getThumbnailImgUrl())
        .detailImgUrl(product.getDetailImgUrl())
        .originalPrice(product.getOriginalPrice())
        .discountPercent(product.getDiscountPercent())
        .discountedPrice(product.getDiscountedPrice())
        .stock(product.getStock())
        .limitCountPerUser(product.getLimitCountPerUser())
        .reviewCount(product.getReviewCount())
        .salesCount(product.getSalesCount())
        .averageRating(product.getAverageRating())
        .isDeleted(product.isDeleted())
        .isPublic(product.isPublic())
        .soldout(product.isSoldout())
        .tags(product.getTags())
        .createdAt(product.getCreatedAt())
        .build();
  }
}
