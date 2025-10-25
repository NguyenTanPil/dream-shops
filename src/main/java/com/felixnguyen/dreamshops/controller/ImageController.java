package com.felixnguyen.dreamshops.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.felixnguyen.dreamshops.dto.ImageDto;
import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.model.Image;
import com.felixnguyen.dreamshops.response.ApiResponse;
import com.felixnguyen.dreamshops.service.image.IImageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Image APIs", description = "APIs for managing images")
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
  private final IImageService imageService;

  @GetMapping("/all")
  public ResponseEntity<ApiResponse> getAllImages() {
    List<ImageDto> images = imageService.getAllImages();
    return ResponseEntity.ok(new ApiResponse("Images retrieved successfully", images));
  }

  @PostMapping("/upload")
  public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
    try {
      List<ImageDto> imageDtos = imageService.saveImage(files, productId);
      return ResponseEntity.ok(new ApiResponse("Update successfully", imageDtos));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Upload failed: " + e.getMessage(), null));
    }
  }

  @GetMapping("/download/{imageId}")
  public ResponseEntity<?> downloadImage(@PathVariable Long imageId) {
    try {
      Image image = imageService.getOriginImageById(imageId);
      byte[] imageBytes = image.getImage().getBytes(1, (int) image.getImage().length());

      ByteArrayResource resource = new ByteArrayResource(imageBytes);

      return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
          .header("Content-Disposition", "attachment; filename=\"" + image.getFileName() + "\"")
          .contentLength(image.getImage().length())
          .body(resource);
    } catch (SQLException e) {
      throw new RuntimeException("Error retrieving image from database", e);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/{imageId}/delete")
  public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
    ImageDto image = imageService.getImageById(imageId);
    try {
      if (image != null) {
        imageService.deleteImage(imageId);
        return ResponseEntity.ok().body(new ApiResponse("Delete image successful", null));
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Image not found", null));
      }
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Delete failed: " + e.getMessage(), null));
    }
  }

  @PutMapping("/{imageId}/update")
  public ResponseEntity<ApiResponse> updateImage(@RequestParam MultipartFile file, @PathVariable Long imageId) {
    ImageDto image = imageService.getImageById(imageId);

    try {
      if (image != null) {
        imageService.updateImage(file, imageId);
        return ResponseEntity.ok().body(new ApiResponse("Update image successful!", null));
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Image not found", null));
      }
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Update failed: " + e.getMessage(), null));
    }
  }
}
