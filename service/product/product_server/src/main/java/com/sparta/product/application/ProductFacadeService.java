package com.sparta.product.application;

import com.sparta.product.presentation.request.ProductCreateRequest;
import com.sparta.product.presentation.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductFacadeService {
  private final ProductService productService;

  public String createProduct(ProductCreateRequest request) {
    ProductResponse product = productService.createProduct(request);
    // TODO :: insert messaging queue
    return product.getProductId();
  }
}
