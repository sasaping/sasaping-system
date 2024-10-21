package com.sparta.product.application.product;

import com.sparta.product.application.dto.ImgDto;
import com.sparta.product.domain.model.Product;
import com.sparta.product.domain.model.SortOption;
import com.sparta.product.domain.repository.cassandra.ProductRepository;
import com.sparta.product.presentation.exception.ProductErrorCode;
import com.sparta.product.presentation.exception.ProductServerException;
import com.sparta.product.presentation.request.ProductCreateRequest;
import com.sparta.product.presentation.request.ProductUpdateRequest;
import com.sparta.product.presentation.response.ProductResponse;
import com.sparta.product_dto.ProductDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ProductService")
public class ProductService {

  private final ProductRepository productRepository;

  @Transactional
  public ProductResponse createProduct(ProductCreateRequest request, ImgDto imgDto) {
    Product newProduct = ProductMapper.toEntity(request, imgDto);
    newProduct.setIsNew(true);
    Product savedProduct = productRepository.save(newProduct);
    return ProductResponse.fromEntity(savedProduct);
  }

  @Transactional
  public ProductResponse updateProduct(
      ProductUpdateRequest request, Product savedProduct, ImgDto imgUrls) {
    ProductMapper.updateProduct(request, savedProduct, imgUrls);
    productRepository.save(savedProduct);
    return ProductResponse.fromEntity(savedProduct);
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

  @Transactional
  public void reduceStock(Map<String, Integer> productQuantities) {
    log.info(productQuantities.toString());
    productQuantities.entrySet().stream()
        .forEach(
            entry -> {
              log.info(entry.getKey(), entry.getValue());
              String productId = entry.getKey();
              int reduceCount = entry.getValue();
              Product product = getSavedProduct(UUID.fromString(productId));
              validateProductStock(product, reduceCount);
              product.updateStock(reduceCount);
              productRepository.save(product);
            });
  }

  @Transactional
  public void rollbackStock(Map<String, Integer> productQuantities) {
    productQuantities.entrySet().stream()
        .forEach(
            entry -> {
              String productId = entry.getKey();
              int rollbackCount = entry.getValue();
              Product product = getSavedProduct(UUID.fromString(productId));
              product.rollbackStock(rollbackCount);
              productRepository.save(product);
            });
  }

  public ProductResponse getProduct(UUID productId) {
    return ProductResponse.fromEntity(getSavedProduct(productId));
  }

  public Page<ProductResponse> getProductList(
      int page,
      int size,
      Long categoryId,
      String brandName,
      Long minPrice,
      Long maxPrice,
      String productSize,
      String mainColor,
      String sortOption) {
    SortOption sort = SortOption.valueOf(sortOption.toUpperCase());
    Sort.Direction direction =
        sort.getOrder().name().contains("Asc") ? Direction.ASC : Direction.DESC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort.getField()));
    List<ProductResponse> result =
        productRepository
            .findAllByFilters(
                categoryId,
                brandName,
                BigDecimal.valueOf(minPrice),
                BigDecimal.valueOf(maxPrice),
                productSize,
                mainColor,
                pageable)
            .stream()
            .map(ProductResponse::fromEntity)
            .toList();
    return new PageImpl<>(result, pageable, result.size());
  }

  public List<ProductDto> getProductList(List<String> productIds) {
    return productIds.stream()
        .map(productId -> getSavedProduct(UUID.fromString(productId)))
        .map(ProductMapper::fromEntity)
        .collect(Collectors.toList());
  }

  public Product getSavedProduct(UUID productId) {
    return productRepository
        .findByProductIdAndIsDeletedFalse(productId)
        .orElseThrow(() -> new ProductServerException(ProductErrorCode.NOT_FOUND_PRODUCT));
  }

  private void validateProductStock(Product product, int reduceCount) {
    if (product.getStock() < reduceCount) {
      throw new ProductServerException(ProductErrorCode.STOCK_NOT_AVAILABLE);
    }
  }
}
