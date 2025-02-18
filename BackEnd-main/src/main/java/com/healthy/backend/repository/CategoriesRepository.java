package com.healthy.backend.repository;

import com.healthy.backend.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categories, String> {
} 