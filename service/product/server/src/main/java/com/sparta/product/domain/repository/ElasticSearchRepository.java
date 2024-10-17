package com.sparta.product.domain.repository;

import com.sparta.product.infrastructure.utils.ProductSearchDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticSearchRepository
    extends ElasticsearchRepository<ProductSearchDto, String> {}
