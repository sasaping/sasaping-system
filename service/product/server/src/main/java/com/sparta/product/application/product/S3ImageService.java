package com.sparta.product.application.product;

import com.amazonaws.services.s3.AmazonS3;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "S3ImageService")
public class S3ImageService implements ImageService {
  private final AmazonS3 s3Client;

  @Value("${aws.s3.bucket-name.product-origin}")
  private String originBucketName;

  @Value("${aws.s3.bucket-name.product-detail}")
  private String detailBucketName;

  @Override
  public String uploadImage(String type, MultipartFile file) throws IOException {
    String fileName = generateFileName(Objects.requireNonNull(file.getOriginalFilename()));
    log.info("fileName: {}", fileName);
    if (type.equals("origin")) {
      s3Client.putObject(originBucketName, fileName, file.getInputStream(), null);
      return s3Client.getUrl(originBucketName, fileName).toString();
    } else if (type.equals("detail")) {
      s3Client.putObject(detailBucketName, fileName, file.getInputStream(), null);
      return s3Client.getUrl(detailBucketName, fileName).toString();
    }
    return null;
  }

  @Override
  public String generateFileName(String originName) {
    String extension = originName.substring(originName.lastIndexOf("."));
    return UUID.randomUUID().toString() + extension;
  }

  @Override
  public void deleteImage(String imgUrl) {
    String[] parts = imgUrl.split("/");
    String bucketName = parts[2].split("\\.")[0];
    String fileName = parts[parts.length - 1];
    s3Client.deleteObject(bucketName, fileName);
  }
}
