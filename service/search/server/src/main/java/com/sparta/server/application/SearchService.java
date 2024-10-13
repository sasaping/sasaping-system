package com.sparta.server.application;

import com.sparta.server.domain.ProductSearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {

  private final ElasticsearchOperations elasticsearchOperations;

  public SearchHits<ProductSearchDto> searchProducts(String keyword, int page, int size) {
    NativeQuery searchQuery = NativeQuery.builder()
        .withQuery(q -> q
            .multiMatch(m -> m
                .query(keyword)
                .fields("productName", "brandName", "description", "tags")
            )
        )
        .withPageable(PageRequest.of(page, size))
        .build();

    return elasticsearchOperations.search(searchQuery, ProductSearchDto.class);
  }


}
