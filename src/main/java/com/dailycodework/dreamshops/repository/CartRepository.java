package com.dailycodework.dreamshops.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.dreamshops.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

  Optional<Cart> findByUserId(Long userId);


}
