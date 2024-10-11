package com.sparta.product.application.product;

import com.sparta.product.domain.model.SortOption;
import com.sparta.product.domain.repository.ElasticSearchRepository;
import com.sparta.product.domain.repository.ElasticsearchCustomRepository;
import com.sparta.product.infrastructure.elasticsearch.dto.ProductSearchDto;
import com.sparta.product.presentation.response.ProductResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ElasticsearchService")
public class ElasticsearchService {
  private final ElasticSearchRepository elasticsearchRepository;
  private final ElasticsearchCustomRepository customRepository;

  @Async
  public void saveProduct(ProductResponse response) {
    ProductSearchDto product = ProductSearchDto.toDto(response);
    elasticsearchRepository.save(product);
    log.info("save product in elastic search {}", product.getProductId());
  }

  @Async
  public void updateProduct(ProductResponse response) {
    ProductSearchDto product = ProductSearchDto.toDto(response);
    elasticsearchRepository.save(product); // 덮어쓰기 방식
    log.info("update product in elastic search {}", product.getProductId());
  }

  @Async
  public void deleteProduct(ProductResponse response) {
    ProductSearchDto product = ProductSearchDto.toDto(response);
    elasticsearchRepository.delete(product);
    log.info("delete product in elastic search {}", product.getProductId());
  }

  public Page<ProductSearchDto> getProductList(
      int page,
      int size,
      Long categoryId,
      String brandName,
      Long minPrice,
      Long maxPrice,
      String productSize,
      String mainColor,
      String sortOption)
      throws IOException {
    SortOption sort = SortOption.valueOf(sortOption.toUpperCase());
    Pageable pageable = PageRequest.of(page, size, Sort.by(sort.getField()));
    return customRepository.searchProductList(
        categoryId,
        brandName,
        minPrice,
        maxPrice,
        productSize,
        mainColor,
        page,
        size,
        pageable,
        sort);
  }
}
