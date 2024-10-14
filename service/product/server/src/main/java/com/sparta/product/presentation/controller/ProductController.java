package com.sparta.product.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.product.application.product.ProductFacadeService;
import com.sparta.product.application.product.ProductService;
import com.sparta.product.presentation.request.ProductCreateRequest;
import com.sparta.product.presentation.request.ProductUpdateRequest;
import com.sparta.product.presentation.response.ProductResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {
  private final ProductFacadeService facadeService;
  private final ProductService productService;

  @PostMapping
  public ApiResponse<String> createProduct(@RequestBody @Valid ProductCreateRequest request) {
    return ApiResponse.created(facadeService.createProduct(request));
  }

  @PatchMapping
  public ApiResponse<ProductResponse> updateProduct(
      @RequestBody @Valid ProductUpdateRequest request) {
    return ApiResponse.ok(facadeService.updateProduct(request));
  }

  @PatchMapping("/{productId}")
  public ApiResponse<ProductResponse> updateStatus(
      @PathVariable("productId") @NotNull UUID productId, @RequestParam("soldout") boolean status) {
    return ApiResponse.ok(facadeService.updateStatus(productId, status));
  }

  @DeleteMapping("/{productId}")
  public ApiResponse<Boolean> deleteProduct(@PathVariable("productId") @NotNull UUID productId) {
    return ApiResponse.ok(facadeService.deleteProduct(productId));
  }

  @GetMapping("/{productId}")
  public ApiResponse<ProductResponse> getProduct(
      @PathVariable("productId") @NotNull UUID productId) {
    return ApiResponse.ok(productService.getProduct(productId));
  }

  @GetMapping
  public ApiResponse<Page<ProductResponse>> getProductList(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size,
      @RequestParam("categoryId") Long categoryId,
      @RequestParam(value = "brandName", required = false) String brandName,
      @RequestParam(name = "minPrice", defaultValue = "1000") @Min(1000) Long minPrice,
      @RequestParam(value = "maxPrice", required = false) Long maxPrice,
      @RequestParam(value = "productSize", required = false) String productSize,
      @RequestParam(value = "mainColor", required = false) String mainColor,
      @RequestParam(value = "sort", defaultValue = "newest") String sortOption)
      throws IOException {
    return ApiResponse.ok(
        productService.getProductList(
            page,
            size,
            categoryId,
            brandName,
            minPrice,
            maxPrice,
            productSize,
            mainColor,
            sortOption));
  }
}
