package com.sparta.product.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.product.application.product.ElasticsearchService;
import com.sparta.product.infrastructure.utils.ProductSearchDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ProductSearchController {
  private final ElasticsearchService elasticSearchService;

  @GetMapping("/api/products/search")
  public ApiResponse<Page<ProductSearchDto>> getProductList(
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
        elasticSearchService.getProductList(
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
