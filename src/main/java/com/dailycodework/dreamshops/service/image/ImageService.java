package com.dailycodework.dreamshops.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dailycodework.dreamshops.dto.ImageDto;
import com.dailycodework.dreamshops.exceptions.ImageNotFoundException;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.ImageRepository;
import com.dailycodework.dreamshops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

  private final ImageRepository imageRepository;
  private final IProductService productService;

  @Override
  public Image getImageById(Long id) {
    return imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException("Image: " + id + " not found"));
  }

  @Override
  public void deleteImageById(Long id) {
    imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,
        () -> new ImageNotFoundException("Image: " + id + " not found"));
  }

  @Override
  public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
    Product product = productService.getProductById(productId);
    String url = "api/v1/images/image/";
    List<ImageDto> savedImageDto = new ArrayList<>();

    try {
      for (MultipartFile file : files) {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setImage(new SerialBlob(file.getBytes()));
        image.setProduct(product);
        Image savedImage = imageRepository.save(image);
        savedImage.setDownloadUrl(url + savedImage.getId());
        imageRepository.save(savedImage);
        ImageDto imageDto = new ImageDto(savedImage.getId(), savedImage.getFileName(), savedImage.getDownloadUrl());
        savedImageDto.add(imageDto);
      }
    } catch (IOException | SQLException e) {
      throw new RuntimeException(e.getMessage());
    }

    return savedImageDto;
  }

  @Override
  public void updateImage(MultipartFile file, Long imageId) {
    Image image = getImageById(imageId);
    try {
      image.setFileName(file.getOriginalFilename());
      image.setFileType(file.getContentType());
      image.setImage(new SerialBlob(file.getBytes()));
      imageRepository.save(image);
    } catch (IOException | SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

}
