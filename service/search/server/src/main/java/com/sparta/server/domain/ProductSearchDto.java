package com.sparta.server.domain;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@NoArgsConstructor
@Document(indexName = "sasaping-ecommerce-products")
public class ProductSearchDto {

  @Id
  private String productId;
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
  private Long reviewCount;
  private Long salesCount;
  private boolean isDeleted;
  private String createdAt;


}
