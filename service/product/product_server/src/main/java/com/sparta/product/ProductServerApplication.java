package com.sparta.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProductServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductServerApplication.class, args);
  }
}
