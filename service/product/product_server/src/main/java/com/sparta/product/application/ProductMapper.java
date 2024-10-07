package com.sparta.product.application;

import com.sparta.product.domain.model.Product;
import com.sparta.product.presentation.request.ProductCreateRequest;
import com.sparta.product.presentation.request.ProductUpdateRequest;

public class ProductMapper {
  public static Product toEntity(ProductCreateRequest request) {
    return Product.builder()
        .categoryId(request.categoryId())
        .productName(request.productName())
        .description(request.description())
        .originalPrice(request.originalPrice())
        .discountPercent(request.discountPercent())
        .thumbnailImgUrl(request.thumbnailImgUrl())
        .detailImgUrl(request.detailImgUrl())
        .stock(request.stock())
        .limitCountPerUser(request.limitCountPerUser())
        .isCoupon(request.isCoupon())
        .build();
  }

  public static void updateProduct(ProductUpdateRequest request, Product existingProduct) {
    existingProduct.updateProduct(
        request.categoryId(),
        request.productName(),
        request.originalPrice(),
        request.discountPercent(),
        request.stock(),
        request.description(),
        request.thumbnailImgUrl(),
        request.detailImgUrl(),
        request.limitCountPerUser(),
        request.isPublic(),
        request.isCoupon());
  }
}
