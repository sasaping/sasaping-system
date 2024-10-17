package com.sparta.product.application.product;

import com.sparta.product.application.category.CategoryService;
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
@Transactional
public class ProductFacadeService {
  private final ProductService productService;
  private final CategoryService categoryService;
  private final ElasticsearchService elasticSearchService;

  public String createProduct(ProductCreateRequest request) {
    if (!categoryService.existsCategory(request.categoryId())) {
      throw new ProductServerException(ProductErrorCode.NOT_FOUND_CATEGORY);
    }
    ProductResponse product = productService.createProduct(request);
    elasticSearchService.saveProduct(product);
    return product.getProductId();
  }

  public ProductResponse updateProduct(ProductUpdateRequest request) {
    if (!categoryService.existsCategory(request.categoryId())) {
      throw new ProductServerException(ProductErrorCode.NOT_FOUND_CATEGORY);
    }
    ProductResponse product = productService.updateProduct(request);
    elasticSearchService.updateProduct(product);
    return product;
  }

  public ProductResponse updateStatus(UUID productId, boolean status) {
    ProductResponse product = productService.updateStatus(productId, status);
    elasticSearchService.updateProduct(product);
    return product;
  }

  public boolean deleteProduct(UUID productId) {
    ProductResponse product = productService.deleteProduct(productId);
    elasticSearchService.deleteProduct(product);
    return product.isDeleted();
  }
}
