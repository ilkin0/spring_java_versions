package com.ilkinmehdiyev.java11.repository;

import com.ilkinmehdiyev.java11.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
