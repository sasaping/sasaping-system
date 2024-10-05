package com.sparta.product.application;

import com.sparta.product.domain.model.Product;
import com.sparta.product.domain.repository.cassandra.ProductRepository;
import com.sparta.product.presentation.exception.ProductErrorCode;
import com.sparta.product.presentation.exception.ProductServerException;
import com.sparta.product.presentation.request.ProductCreateRequest;
import com.sparta.product.presentation.request.ProductUpdateRequest;
import com.sparta.product.presentation.response.ProductResponse;
import java.util.UUID;
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

  @Transactional
  public ProductResponse updateProduct(ProductUpdateRequest request) {
    Product product = getSavedProduct(request.productId());
    ProductMapper.updateProduct(request, product);
    productRepository.save(product);
    return ProductResponse.fromEntity(product);
  }

  @Transactional
  public ProductResponse updateStatus(UUID productId, boolean status) {
    Product product = getSavedProduct(productId);
    product.setSoldout(status);
    productRepository.save(product);
    return ProductResponse.fromEntity(product);
  }

  @Transactional
  public ProductResponse deleteProduct(UUID productId) {
    Product product = getSavedProduct(productId);
    product.isDelete();
    productRepository.save(product);
    return ProductResponse.fromEntity(product);
  }

  private Product getSavedProduct(UUID productId) {
    return productRepository
        .findByProductIdAndIsDeletedFalse(productId)
        .orElseThrow(() -> new ProductServerException(ProductErrorCode.NOT_FOUND_PRODUCT));
  }
}
