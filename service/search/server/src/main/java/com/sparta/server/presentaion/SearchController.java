package com.sparta.server.presentaion;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.server.application.SearchService;
import com.sparta.server.domain.ProductSearchDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SearchController {

  private final SearchService searchService;

  @GetMapping("/search")
  public ApiResponse<List<ProductSearchDto>> searchProducts(
      @RequestParam String keyword,
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) String brandName,
      @RequestParam(required = false) String mainColor,
      @RequestParam(required = false) Double minPrice,
      @RequestParam(required = false) Double maxPrice,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    SearchHits<ProductSearchDto> searchHits = searchService.searchProducts(keyword, categoryId,
        brandName, mainColor, minPrice, maxPrice, page, size);
    List<ProductSearchDto> products = searchHits.getSearchHits().stream()
        .map(SearchHit::getContent)
        .collect(Collectors.toList());
    return ApiResponse.ok(products);
  }


}
