package com.sparta.product.infrastructure.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class S3Config {

  @Value("${aws.credentials.access-key}")
  public String accessKey;

  @Value("${aws.credentials.secret-key}")
  public String secretKey;

  @Value("${aws.credentials.region}")
  public String s3Region;

  @Bean
  @Lazy
  public AmazonS3 s3Client() {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    return AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .withRegion(s3Region)
        .build();
  }
}
