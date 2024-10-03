package com.sparta.product.application;

import com.sparta.product.infrastructure.elasticsearch.dto.ProductSearchDto;
import com.sparta.product.presentation.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ElasticSearchService")
public class ElasticSearchService {
  private final ElasticsearchRepository elasticsearchRepository;

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
}
