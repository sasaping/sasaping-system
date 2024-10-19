package com.sparta.product.application.product;

import com.sparta.product.application.category.CategoryService;
import com.sparta.product.application.dto.ImgDto;
import com.sparta.product.domain.model.Product;
import com.sparta.product.presentation.exception.ProductErrorCode;
import com.sparta.product.presentation.exception.ProductServerException;
import com.sparta.product.presentation.request.ProductCreateRequest;
import com.sparta.product.presentation.request.ProductUpdateRequest;
import com.sparta.product.presentation.response.ProductResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ProductFacadeService")
public class ProductFacadeService {
  private final ProductService productService;
  private final CategoryService categoryService;
  private final ElasticsearchService elasticSearchService;
  private final S3ImageService imageService;

  @Transactional
  public String createProduct(
      ProductCreateRequest request, MultipartFile productImg, MultipartFile detailImg)
      throws IOException {
    validateCategoryId(request.categoryId());
    String productImgUrl = imageService.uploadImage("origin", productImg);
    String detailImgUrl = imageService.uploadImage("detail", detailImg);
    ProductResponse product = productService.createProduct(request, productImgUrl, detailImgUrl);
    elasticSearchService.saveProduct(product);
    return product.getProductId();
  }

  @Transactional
  public ProductResponse updateProduct(
      ProductUpdateRequest request, MultipartFile productImg, MultipartFile detailImg)
      throws IOException {
    validateCategoryId(request.categoryId());
    Product savedProduct = productService.getSavedProduct(request.productId());

    ImgDto imgData = fetchImgUrls(savedProduct, productImg, detailImg);
    ProductResponse newProduct = productService.updateProduct(request, savedProduct, imgData);
    elasticSearchService.updateProduct(newProduct);
    return newProduct;
  }

  @Transactional
  public ProductResponse updateStatus(UUID productId, boolean status) {
    ProductResponse product = productService.updateStatus(productId, status);
    elasticSearchService.updateProduct(product);
    return product;
  }

  @Transactional
  public boolean deleteProduct(UUID productId) {
    ProductResponse product = productService.deleteProduct(productId);
    elasticSearchService.deleteProduct(product);
    Optional.ofNullable(product.getOriginImgUrl()).ifPresent(imageService::deleteImage);
    Optional.ofNullable(product.getDetailImgUrl()).ifPresent(imageService::deleteImage);
    Optional.ofNullable(product.getThumbnailImgUrl()).ifPresent(imageService::deleteImage);
    return product.isDeleted();
  }

  private ImgDto fetchImgUrls(
      Product savedProduct, MultipartFile productImg, MultipartFile detailImg) throws IOException {
    String productImgUrl = savedProduct.getOriginImgUrl();
    String detailImgUrl = savedProduct.getDetailImgUrl();
    if (productImg != null && !productImg.isEmpty()) {
      imageService.deleteImage(productImgUrl);
      productImgUrl = imageService.uploadImage("origin", productImg);
    }
    if (detailImg != null && !detailImg.isEmpty()) {
      imageService.deleteImage(detailImgUrl);
      detailImgUrl = imageService.uploadImage("detail", detailImg);
    }
    return new ImgDto(productImgUrl, detailImgUrl);
  }

  private void validateCategoryId(Long categoryId) {
    if (!categoryService.existsCategory(categoryId)) {
      throw new ProductServerException(ProductErrorCode.NOT_FOUND_CATEGORY);
    }
  }
}
