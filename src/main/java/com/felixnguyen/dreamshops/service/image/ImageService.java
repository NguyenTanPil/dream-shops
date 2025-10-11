package com.felixnguyen.dreamshops.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.modelmapper.ModelMapper;
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
  private final ModelMapper modelMapper;

  @SuppressWarnings("PMD.UnusedPrivateMethod")
  private ImageDto convertToDto(Image image) {
    return modelMapper.map(image, ImageDto.class);
  }

  @Override
  public ImageDto getImageById(Long id) {
    return imageRepository.findById(id).map(this::convertToDto)
        .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));
  }

  @Override
  public Image getOriginImageById(Long id) {
    return imageRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));
  }

  @Override
  public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
    Product product = productService.getOriginProductById(productId);
    List<ImageDto> savedImageDtos = new ArrayList<ImageDto>();

    for (MultipartFile file : files) {
      try {
        Image image = new Image();
        image.setImage(new SerialBlob(file.getBytes()));
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setProduct(product);

        String buildDownloadUrl = "/api/v1/images/download/";
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
    Image image = imageRepository.findById(id).orElse(null);
    if (image != null) {
      try {
        image.setImage(new SerialBlob(file.getBytes()));
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        imageRepository.save(image);
      } catch (IOException | SQLException e) {
        throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", e);
      }
    } else {
      throw new ResourceNotFoundException("Image not found with id: " + id);
    }
  }

  @Override
  public List<ImageDto> getAllImages() {
    return imageRepository.findAll().stream().map(this::convertToDto).toList();
  }

}
