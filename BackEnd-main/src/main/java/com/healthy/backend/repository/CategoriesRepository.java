package com.healthy.backend.repository;

import com.healthy.backend.entity.Categories;
import com.healthy.backend.enums.SurveyCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, String> {
    Categories findByCategoryName(SurveyCategory categoryName);
}