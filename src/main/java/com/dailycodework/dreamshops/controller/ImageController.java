package com.dailycodework.dreamshops.controller;

import java.net.http.HttpHeaders;
import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dailycodework.dreamshops.dto.ImageDto;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.image.IImageService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * ImageController is a REST controller that handles HTTP requests related to
 * image resources.
 * It uses the IImageService to perform operations on images.
 * 
 * Annotations:
 * 
 */

// Indicates that this class is a REST controller and will handle HTTP requests.
@RestController
// ("${api.prefix}/images") - Maps HTTP requests to /images endpoint, with a
// prefix defined in the application properties.
@RequestMapping("${api.prefix}/images")
// Generates a constructor with required arguments (final fields) for dependency
// injection.
@RequiredArgsConstructor
public class ImageController {
  private final IImageService imageService;

  @PostMapping("/upload")
  public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
    try {
      List<ImageDto> imageDtos = imageService.saveImages(files, productId);
      return ResponseEntity.ok(new ApiResponse("Upload success!🥳", imageDtos));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ApiResponse("Upload failed!😵‍💫", e.getMessage()));
    }
  }

  // Not-finished
  public ResponseEntity<ApiResponse> downloadImage(@PathVariable Long imageId) throws SQLException {
    Image image = imageService.getImageById(imageId);
    ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
  }
}
