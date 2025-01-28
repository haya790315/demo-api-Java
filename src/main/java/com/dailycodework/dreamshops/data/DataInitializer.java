package com.dailycodework.dreamshops.data;

import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.model.Role;
// import com.dailycodework.dreamshops.model.Role;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import com.dailycodework.dreamshops.repository.ImageRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
  private final UserRepository userRepository;

  private final CategoryRepository categoryRepository;

  private final ProductRepository productRepository;

  private final ImageRepository imageRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
    Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
    createDefaultRoleIfNotExits(defaultRoles);
    createDefaultUserIfNotExits();
    createDefaultAdminIfNotExits();
    createDefaultProducts();
  }

  private void createDefaultUserIfNotExits() {
    Role userRole = roleRepository.findByName("ROLE_USER").get();
    for (int i = 1; i <= 3; i++) {
      String defaultEmail = "user" + i + "@email.com";
      if (userRepository.existsByEmail(defaultEmail)) {
        continue;
      }
      User user = new User();
      user.setFirstName("The User");
      user.setLastName("User" + i);
      user.setEmail(defaultEmail);
      user.setPassword(passwordEncoder.encode("87654321"));
      // user.setPassword("123456");
      user.setRoles(Set.of(userRole));
      userRepository.save(user);
      System.out.println("Default vet user " + i + " created successfully.");
    }
  }

  private void createDefaultAdminIfNotExits() {
    Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
    for (int i = 1; i <= 2; i++) {
      String defaultEmail = "admin" + i + "@email.com";
      if (userRepository.existsByEmail(defaultEmail)) {
        continue;
      }
      User user = new User();
      user.setFirstName("Admin");
      user.setLastName("Admin" + i);
      user.setEmail(defaultEmail);
      // user.setPassword("123456");
      user.setPassword(passwordEncoder.encode("123456"));
      user.setRoles(Set.of(adminRole));
      userRepository.save(user);
      System.out.println("Default admin user " + i + " created successfully.");
    }
  }

  private void createDefaultRoleIfNotExits(Set<String> roles) {
    roles.stream()
        .filter(role -> roleRepository.findByName(role).isEmpty())
        .map(Role::new).forEach(roleRepository::save);
  }

  private void createDefaultProducts() {
    // Create default categories
    String[] categories = new String[] { "SnowBoard", "Binding", "Goggles", "Boots", "Helmet" };
    for (String cate : categories) {
      if (!categoryRepository.existsByName(cate)) {
        categoryRepository.save(new Category(cate));
      }
    }
    if (categoryRepository.count() == categories.length) {
      System.out.println("Default categories created successfully.");
    }

    // Create default products
    Product[] products = new Product[] {
        new Product("Custom", "Burton", new BigDecimal(1000), 100,
            "The Burton Custom",
            categoryRepository.findByName("SnowBoard")),
        new Product("Telex", "Flux", new BigDecimal(600), 50,
            "The Flux Bindings",
            categoryRepository.findByName("Binding")),
        new Product("Extreme", "Union", new BigDecimal(200), 87,
            "The Union Bindings",
            categoryRepository.findByName("Boots")),
        new Product("Fire on", "Burton", new BigDecimal(500), 10,
            "Good choice",
            categoryRepository.findByName("Goggles")),
        new Product("Invincible", "OoK", new BigDecimal(300), 120,
            "Nice choice",
            categoryRepository.findByName("Helmet")),
    };

    for (Product product : products) {
      if (productRepository.existsByName(product.getName())) {
        continue;
      }
      productRepository.save(product);
      if (productRepository.count() == products.length) {
        System.out.println("Default products created successfully.");
      }
    }

    // Create default images
    Image[] images = new Image[] {
        Image.builder().fileName("Ride_on1.jpg").fileType("image/jpeg")
            .downloadUrl("Ride_on1.jpg")
            .product(productRepository.findById(1L).orElse(null)).build(),
        Image.builder().fileName("Ride_on2.jpg").fileType("image/jpeg")
            .downloadUrl("Ride_on2.jpg")
            .product(productRepository.findById(1L).orElse(null)).build(),
        Image.builder().fileName("Extreme.jpg").fileType("image/jpeg")
            .downloadUrl("Extreme.jpg")
            .product(productRepository.findById(3L).orElse(null)).build(),
        Image.builder().fileName("Invincible.jpg").fileType("image/jpeg")
            .downloadUrl("Invincible.jpg")
            .product(productRepository.findById(5L).orElse(null)).build(),
    };

    for (Image image : images) {
      if (imageRepository.existsByFileName(image.getFileName())) {
        continue;
      }
      imageRepository.save(image);
      if (imageRepository.count() == images.length) {
        System.out.println("Default images created successfully.");
      }
    }

  }

  // private void createDefaultAdminIfNotExits() {
  // Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
  // for (int i = 1; i <= 2; i++) {
  // String defaultEmail = "admin" + i + "@email.com";
  // if (userRepository.existsByEmail(defaultEmail)) {
  // continue;
  // }
  // User user = new User();
  // user.setFirstName("Admin");
  // user.setLastName("Admin" + i);
  // user.setEmail(defaultEmail);
  // user.setPassword(passwordEncoder.encode("123456"));
  // user.setRoles(Set.of(adminRole));
  // userRepository.save(user);
  // System.out.println("Default admin user " + i + " created successfully.");
  // }
  // }

  // private void createDefaultRoleIfNotExits(Set<String> roles) {
  // roles.stream()
  // .filter(role -> roleRepository.findByName(role).isEmpty())
  // .map(Role::new).forEach(roleRepository::save);

  // }

}