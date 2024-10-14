package com.sparta.server.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum SortOption {
  RELEVANCE("_score", Sort.Direction.DESC),
  PRICE_LOW_TO_HIGH("discountedPrice", Sort.Direction.ASC),
  PRICE_HIGH_TO_LOW("discountedPrice", Sort.Direction.DESC),
  NEWEST("createdAt", Sort.Direction.DESC),
  OLDEST("createdAt", Sort.Direction.ASC),
  NAME_A_TO_Z("productName.keyword", Sort.Direction.ASC),
  NAME_Z_TO_A("productName.keyword", Sort.Direction.DESC),
  REVIEW("reviewCount", Sort.Direction.DESC),
  BEST_SELLING("salesCount", Sort.Direction.DESC);

  private final String field;
  private final Sort.Direction direction;

  public Sort getSort() {
    return Sort.by(direction, field);
  }
}
