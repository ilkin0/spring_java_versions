package com.ilkinmehdiyev.java8.repository;

import com.ilkinmehdiyev.java8.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
