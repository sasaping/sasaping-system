package com.sparta.product.application;

import com.sparta.product.domain.model.Product;
import com.sparta.product.domain.repository.cassandra.ProductRepository;
import com.sparta.product.presentation.request.ProductCreateRequest;
import com.sparta.product.presentation.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;

  @Transactional
  public ProductResponse createProduct(ProductCreateRequest request) {
    Product newProduct = ProductMapper.toEntity(request);
    newProduct.setIsNew(true);
    Product savedProduct = productRepository.save(newProduct);
    return ProductResponse.fromEntity(savedProduct);
  }
}
