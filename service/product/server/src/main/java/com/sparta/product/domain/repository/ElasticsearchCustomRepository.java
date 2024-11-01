package com.sparta.product.domain.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.JsonData;
import com.sparta.product.domain.model.SortOption;
import com.sparta.product.infrastructure.utils.ProductSearchDto;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j(topic = "ElasticsearchCustomRepository")
public class ElasticsearchCustomRepository {
  private final ElasticsearchClient esClient;

  @Value("${product.search-index}")
  private String searchIndex;

  public Page<ProductSearchDto> searchProductList(
      Long categoryId,
      String brandName,
      Long minPrice,
      Long maxPrice,
      String productSize,
      String mainColor,
      int page,
      int pageSize,
      Pageable pageable,
      SortOption sortOption)
      throws IOException {

    List<Query> mustQueries = new ArrayList<>();

    if (categoryId != null) {
      mustQueries.add(MatchQuery.of(m -> m.field("categoryId").query(categoryId))._toQuery());
    }
    if (brandName != null) {
      mustQueries.add(MatchQuery.of(m -> m.field("brandName").query(brandName))._toQuery());
    }
    if (minPrice != null && maxPrice != null) {
      mustQueries.add(
          RangeQuery.of(
                  r ->
                      r.field("discountedPrice")
                          .gte(JsonData.of(BigDecimal.valueOf(minPrice)))
                          .lte(JsonData.of(BigDecimal.valueOf(maxPrice))))
              ._toQuery());
    }
    if (mainColor != null) {
      mustQueries.add(MatchQuery.of(m -> m.field("mainColor").query(mainColor))._toQuery());
    }
    if (productSize != null) {
      mustQueries.add(MatchQuery.of(m -> m.field("size").query(productSize))._toQuery());
    }

    SearchResponse<ProductSearchDto> response =
        esClient.search(
            s ->
                s.index(searchIndex)
                    .query(q -> q.bool(b -> b.must(mustQueries)))
                    .from(Math.max(0, (page - 1) * pageSize))
                    .size(pageSize)
                    .sort(
                        option ->
                            option.field(
                                f -> f.field(sortOption.getField()).order(sortOption.getOrder()))),
            ProductSearchDto.class);

    TotalHits totalHits = response.hits().total();
    List<Hit<ProductSearchDto>> hits = response.hits().hits();
    List<ProductSearchDto> products = new ArrayList<>();
    for (Hit<ProductSearchDto> hit : hits) {
      ProductSearchDto product = hit.source();
      products.add(product);
    }
    return new PageImpl<>(products, pageable, totalHits.value());
  }
}
