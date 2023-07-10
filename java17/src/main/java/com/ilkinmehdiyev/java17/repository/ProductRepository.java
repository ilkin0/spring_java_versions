package com.ilkinmehdiyev.java17.repository;

import com.ilkinmehdiyev.java17.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
