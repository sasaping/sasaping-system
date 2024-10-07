package com.sparta.product.infrastructure.elasticsearch.dto;

import com.sparta.product.presentation.response.ProductResponse;
import java.math.BigDecimal;
import java.util.List;
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
  private String brandName;
  private String mainColor;
  private String size;
  private BigDecimal originalPrice;
  private BigDecimal discountedPrice;
  private Double discountPercent;
  private Integer stock;
  private String description;
  private String thumbnailImgUrl;
  private double averageRating;
  private boolean isPublic;
  private boolean soldout;
  private List<String> tags;
  private boolean isDeleted;

  @Builder
  private ProductSearchDto(
      String productId,
      Long categoryId,
      String productName,
      String brandName,
      String mainColor,
      String size,
      BigDecimal originalPrice,
      BigDecimal discountedPrice,
      Double discountPercent,
      Integer stock,
      String description,
      String thumbnailImgUrl,
      double averageRating,
      boolean isPublic,
      boolean soldout,
      boolean isDeleted,
      List<String> tags) {
    this.productId = productId;
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
    this.thumbnailImgUrl = thumbnailImgUrl;
    this.averageRating = averageRating;
    this.isPublic = isPublic;
    this.soldout = soldout;
    this.isDeleted = isDeleted;
    this.tags = tags;
  }

  public static ProductSearchDto toDto(ProductResponse product) {
    return ProductSearchDto.builder()
        .productId(product.getProductId())
        .categoryId(product.getCategoryId())
        .productName(product.getProductName())
        .brandName(product.getBrandName())
        .mainColor(product.getMainColor())
        .size(product.getSize())
        .originalPrice(product.getOriginalPrice())
        .discountedPrice(product.getDiscountedPrice())
        .discountPercent(product.getDiscountPercent())
        .stock(product.getStock())
        .description(product.getDescription())
        .thumbnailImgUrl(product.getThumbnailImgUrl())
        .averageRating(product.getAverageRating())
        .isPublic(product.isPublic())
        .isDeleted(product.isDeleted())
        .soldout(product.isSoldout())
        .build();
  }
}
