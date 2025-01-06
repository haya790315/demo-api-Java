package com.dailycodework.dreamshops.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.dailycodework.dreamshops.dto.ImageDto;
import com.dailycodework.dreamshops.exceptions.ImageNotFoundException;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.image.IImageService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

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

  @PostMapping("/image/{imageId}/update")
  public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
    try {
      List<ImageDto> imageDtos = imageService.saveImages(files, productId);
      return ResponseEntity.ok(new ApiResponse("Upload success!🥳", imageDtos));
    } catch (Exception e) {
      return ResponseEntity.status(404).body(new ApiResponse("not found!😵‍💫", e.getMessage()));
    }
  }

  @GetMapping("/image/{imageId}/download")
  public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable Long imageId) throws SQLException {
    Image image = imageService.getImageById(imageId);
    ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
        .body(resource);
  }

  @PutMapping("/images/{imageId}/update")
  public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
    try {
      Image image = imageService.getImageById(imageId);
      if (image != null) {
        imageService.updateImage(file, imageId);
        return ResponseEntity.ok(new ApiResponse("Update success! 🥳", null));
      }
    } catch (ImageNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
    return ResponseEntity.internalServerError()
        .body(new ApiResponse("Update Failed 😵‍💫", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  @DeleteMapping("/images/{imageId}/delete")
  public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
    try {
      Image image = imageService.getImageById(imageId);
      if (image != null) {
        imageService.deleteImageById(imageId);
        return ResponseEntity.ok(new ApiResponse("Delete success! 🥳", null));
      }
    } catch (ImageNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiResponse("Update Failed 😵‍💫", HttpStatus.INTERNAL_SERVER_ERROR));
  }
}