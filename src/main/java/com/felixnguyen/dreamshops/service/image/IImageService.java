package com.felixnguyen.dreamshops.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.felixnguyen.dreamshops.dto.ImageDto;
import com.felixnguyen.dreamshops.model.Image;

public interface IImageService {
  Image getImageById(Long id);

  List<ImageDto> saveImage(List<MultipartFile> file, Long productId);

  void deleteImage(Long id);

  void updateImage(MultipartFile file, Long id);
}
