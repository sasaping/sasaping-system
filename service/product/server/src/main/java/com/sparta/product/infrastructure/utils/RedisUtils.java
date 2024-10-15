package com.sparta.product.infrastructure.utils;

public class RedisUtils {
  public static String getRedisKeyOfPreOrder(long preOrderId) {
    return "preorder.request.%s".formatted(preOrderId);
  }
}
