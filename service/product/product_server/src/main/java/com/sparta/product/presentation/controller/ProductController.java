package com.sparta.product.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.product.application.ProductFacadeService;
import com.sparta.product.presentation.request.ProductCreateRequest;
import com.sparta.product.presentation.request.ProductUpdateRequest;
import com.sparta.product.presentation.response.ProductResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
}
