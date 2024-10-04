package com.sparta.slack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SlackApplication {

  public static void main(String[] args) {
    SpringApplication.run(SlackApplication.class, args);
  }

}
