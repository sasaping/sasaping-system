package com.sparta.server.application;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;
import com.sparta.server.domain.ProductSearchDto;
import com.sparta.server.domain.SortOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
      String brandName, String mainColor, Double minPrice, Double maxPrice, String size,
      SortOption sortOption, int page,
      int pageSize) {
    BoolQuery.Builder boolQuery = new BoolQuery.Builder();

    boolQuery.must(q -> q
        .queryString(qs -> qs
            .query(String.format("*%s*", keyword))
            .fields("productName", "brandName", "description", "tags")
        )
    );

    if (categoryId != null) {
      boolQuery.filter(q -> q.term(t -> t.field("categoryId").value(categoryId)));
    }

    if (brandName != null && !brandName.isEmpty()) {
      boolQuery.filter(q -> q.term(t -> t.field("brandName.keyword").value(brandName)));
    }

    if (mainColor != null && !mainColor.isEmpty()) {
      boolQuery.filter(q -> q.term(t -> t.field("mainColor.keyword").value(mainColor)));
    }

    if (size != null && !size.isEmpty()) {
      boolQuery.filter(q -> q.term(t -> t.field("size.keyword").value(size)));
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
        .withQuery(boolQuery.build()._toQuery())
        .withPageable(PageRequest.of(page, pageSize))
        .build();

    if (sortOption != null && !sortOption.equals(SortOption.RELEVANCE)) {
      Sort sort = sortOption.getSort();
      searchQuery.setSort(sort);
    }

    return elasticsearchOperations.search(searchQuery, ProductSearchDto.class);
  }


}
