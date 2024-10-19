package com.sparta.product.application.product;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
  public String uploadImage(MultipartFile file) throws IOException;

  public String generateFileName(String originName);
}
