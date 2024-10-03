package com.sparta.product.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.product.application.ProductFacadeService;
import com.sparta.product.presentation.request.ProductCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductFacadeService facadeService;

  @PostMapping
  public ApiResponse<String> createProduct(@RequestBody @Validated ProductCreateRequest request) {
    return ApiResponse.created(facadeService.createProduct(request));
  }
}
