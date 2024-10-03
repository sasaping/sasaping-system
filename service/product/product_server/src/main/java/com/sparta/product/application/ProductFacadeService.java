package com.sparta.product.application;

import com.sparta.product.presentation.request.ProductCreateRequest;
import com.sparta.product.presentation.request.ProductUpdateRequest;
import com.sparta.product.presentation.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductFacadeService {
  private final ProductService productService;
  private final ElasticSearchService elasticSearchService;

  public String createProduct(ProductCreateRequest request) {
    ProductResponse product = productService.createProduct(request);
    elasticSearchService.saveProduct(product);
    return product.getProductId();
  }

  public ProductResponse updateProduct(ProductUpdateRequest request) {
    ProductResponse product = productService.updateProduct(request);
    elasticSearchService.updateProduct(product);
    return product;
  }
}
