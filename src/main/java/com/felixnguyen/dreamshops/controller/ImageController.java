package com.felixnguyen.dreamshops.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.felixnguyen.dreamshops.dto.ImageDto;
import com.felixnguyen.dreamshops.response.ApiResponse;
import com.felixnguyen.dreamshops.service.image.IImageService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
  private final IImageService imageService;
  private static final int INTERNAL_SERVER_ERROR = 500;

  @PostMapping("/upload")
  public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
    try {
      List<ImageDto> imageDtos = imageService.saveImage(files, productId);
      return ResponseEntity.ok(new ApiResponse("Update successfully", imageDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Update failed: " + e.getMessage(), null));
    }
  }
}
