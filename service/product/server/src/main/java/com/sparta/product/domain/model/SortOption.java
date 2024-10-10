package com.sparta.product.domain.model;

import co.elastic.clients.elasticsearch._types.SortOrder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortOption {
  MAX_PRICE("discountedPrice", SortOrder.Desc),
  MIN_PRICE("discountedPrice", SortOrder.Asc),
  SALES("salesCount", SortOrder.Desc),
  NEWEST("createdAt", SortOrder.Desc),
  REVIEW("reviewCount", SortOrder.Desc);

  private final String field;
  private final SortOrder order;
}
