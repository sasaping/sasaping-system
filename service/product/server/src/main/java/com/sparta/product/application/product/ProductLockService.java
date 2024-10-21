package com.sparta.product.application.product;

import com.sparta.product.application.preorder.DistributedLockComponent;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductLockService {

  private final DistributedLockComponent lockComponent;
  private final ProductService productService;

  @Transactional
  public void reduceStock(Map<String, Integer> productQuantities) {

    Set<String> productIds = productQuantities.keySet();
    lockComponent.executeForMultipleProducts(
        productIds.stream().map("stockLock_%s"::formatted).toList(),
        3000, // 락 대기 시간
        3000, // 점유 시간
        3, // 재시도 횟수
        3000, // 재시도 대기 시간
        () -> {
          productService.reduceStock(productQuantities);
        });
  }

  @Transactional
  public void rollbackStock(Map<String, Integer> productQuantities) {
    Set<String> productIds = productQuantities.keySet();
    lockComponent.executeForMultipleProducts(
        productIds.stream().map("stockLock_%s"::formatted).toList(),
        3000, // 락 대기 시간
        3000, // 점유 시간
        3, // 재시도 횟수
        3000, // 재시도 대기 시간
        () -> {
          productService.rollbackStock(productQuantities);
        });
  }

}
