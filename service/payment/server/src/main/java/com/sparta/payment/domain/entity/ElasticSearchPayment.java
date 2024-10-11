package com.sparta.payment.domain.entity;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "payments")
@Getter
@Setter
@Builder
public class ElasticSearchPayment {

  @Id
  private String paymentId;

  @Field(type = FieldType.Keyword)
  private String userId;

  @Field(type = FieldType.Keyword)
  private String orderName;

  @Field(type = FieldType.Keyword)
  private String orderId;

  @Field(type = FieldType.Date)
  private LocalDateTime createdAt;

  @Field(type = FieldType.Float)
  private float amount;

  @Field(type = FieldType.Keyword)
  private String paymentKey;

  @Field(type = FieldType.Keyword)
  private String state;

}
