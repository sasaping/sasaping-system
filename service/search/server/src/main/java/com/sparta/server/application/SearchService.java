package com.sparta.server.application;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;
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

  public SearchHits<ProductSearchDto> searchProducts(String keyword, Long categoryId,
      String brandName, String mainColor, Double minPrice, Double maxPrice, int page, int size) {

    BoolQuery.Builder boolQuery = new Builder();

    if (categoryId != null) {
      boolQuery.filter(q -> q.term(t -> t.field("categoryId").value(categoryId)));
    }

    if (brandName != null) {
      boolQuery.filter(q -> q.term(t -> t.field("brandName").value(brandName)));
    }

    if (mainColor != null) {
      boolQuery.filter(q -> q.term(t -> t.field("mainColor").value(mainColor)));
    }

    if (minPrice != null || maxPrice != null) {
      RangeQuery.Builder rangeQuery = new RangeQuery.Builder().field("discountedPrice");
      if (minPrice != null) {
        rangeQuery.gte(JsonData.of(minPrice));
      }
      if (maxPrice != null) {
        rangeQuery.lte(JsonData.of(maxPrice));
      }
      boolQuery.filter(q -> q.range(rangeQuery.build()));
    }

    NativeQuery searchQuery = NativeQuery.builder()
        .withQuery(q -> q
            .multiMatch(m -> m
                .query(keyword)
                .fields("productName", "brandName", "description", "tags")
            )
        )
        .withQuery(q -> q.bool(boolQuery.build()))
        .withPageable(PageRequest.of(page, size))
        .build();

    return elasticsearchOperations.search(searchQuery, ProductSearchDto.class);
  }


}
