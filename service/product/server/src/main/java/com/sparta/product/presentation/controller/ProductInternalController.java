package com.sparta.product.presentation.controller;

import com.sparta.product.application.ProductService;
import com.sparta.product_dto.ProductDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/products")
public class ProductInternalController {

  private final ProductService productService;

  @GetMapping
  public List<ProductDto> getProductList(
      @RequestParam(name = "productIds") List<String> productIds) {
    return productService.getProductList(productIds);
  }

}
