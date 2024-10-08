package com.sparta.product.application;

import com.sparta.product.domain.model.Product;
import com.sparta.product.presentation.request.ProductCreateRequest;
import com.sparta.product.presentation.request.ProductUpdateRequest;
import com.sparta.product_dto.ProductDto;

public class ProductMapper {

  public static ProductDto fromEntity(Product product) {
    return ProductDto.builder()
        .productId(product.getProductId())
        .productName(product.getProductName())
        .originalPrice(product.getOriginalPrice())
        .discountPercent(product.getDiscountPercent())
        .discountedPrice(product.getDiscountedPrice())
        .stock(product.getStock())
        .tags(product.getTags())
        .build();
  }

  public static Product toEntity(ProductCreateRequest request) {
    return Product.builder()
        .categoryId(request.categoryId())
        .productName(request.productName())
        .brandName(request.brandName())
        .mainColor(request.mainColor())
        .size(request.size())
        .description(request.description())
        .originalPrice(request.originalPrice())
        .discountPercent(request.discountPercent())
        .thumbnailImgUrl(request.thumbnailImgUrl())
        .detailImgUrl(request.detailImgUrl())
        .stock(request.stock())
        .limitCountPerUser(request.limitCountPerUser())
        .tags(request.tags())
        .build();
  }

  public static void updateProduct(ProductUpdateRequest request, Product existingProduct) {
    existingProduct.updateProduct(
        request.categoryId(),
        request.productName(),
        request.brandName(),
        request.mainColor(),
        request.size(),
        request.originalPrice(),
        request.discountPercent(),
        request.stock(),
        request.description(),
        request.thumbnailImgUrl(),
        request.detailImgUrl(),
        request.limitCountPerUser(),
        request.tags(),
        request.isPublic());
  }

}
