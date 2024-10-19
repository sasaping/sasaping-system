package com.sparta.product.application.product;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
  String uploadImage(String type, MultipartFile file) throws IOException;

  String generateFileName(String originName);

  void deleteImage(String imgUrl);
}
