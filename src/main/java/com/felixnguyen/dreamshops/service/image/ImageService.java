package com.felixnguyen.dreamshops.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.felixnguyen.dreamshops.dto.ImageDto;
import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.model.Image;
import com.felixnguyen.dreamshops.model.Product;
import com.felixnguyen.dreamshops.repository.ImageRepository;
import com.felixnguyen.dreamshops.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
  private final ImageRepository imageRepository;
  private final ProductService productService;

  @Override
  public Image getImageById(Long id) {
    return imageRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));
  }

  @Override
  public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
    Product product = productService.getProductById(productId);
    List<ImageDto> savedImageDtos = new ArrayList<ImageDto>();

    for (MultipartFile file : files) {
      try {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setImage(new SerialBlob(file.getBytes()));
        image.setFileName(file.getContentType());
        image.setProduct(product);

        String buildDownloadUrl = "/api/v1/images/image/download/";
        String downloadUrl = buildDownloadUrl + image.getId();
        image.setDownloadUrl(downloadUrl);

        Image savedImage = imageRepository.save(image);
        savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
        imageRepository.save(savedImage);

        ImageDto imageDto = new ImageDto();
        imageDto.setImageId(savedImage.getId());
        imageDto.setImageName(savedImage.getFileName());
        imageDto.setDownloadUrl(savedImage.getDownloadUrl());
        savedImageDtos.add(imageDto);
      } catch (IOException | SQLException e) {
        throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", e);
      }
    }

    return savedImageDtos;
  }

  @Override
  public void deleteImage(Long id) {
    imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
      throw new ResourceNotFoundException("Image not found with id: " + id);
    });
  }

  @Override
  public void updateImage(MultipartFile file, Long id) {
    Image image = getImageById(id);
    try {
      image.setFileName(file.getOriginalFilename());
      image.setFileName(file.getOriginalFilename());
      image.setImage(new SerialBlob(file.getBytes()));
      imageRepository.save(image);
    } catch (IOException | SQLException e) {
      throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", e);
    }
  }

}
