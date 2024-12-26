package com.dailycodework.dreamshops.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing an image.
 * This class is used to transfer image data between different layers of the
 * application.
 * It contains the image ID, image name, and download URL.
 */
@Data
public class ImageDto {
  private Long imageId;
  private String imageName;
  private String downloadUrl;

  public ImageDto(Long id, String name, String url) {
    this.imageId = id;
    this.imageName = name;
    this.downloadUrl = url;
  }
}
