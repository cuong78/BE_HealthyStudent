package com.healthy.backend.entity;

import com.healthy.backend.enums.SurveyCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Categories")
public class Categories {

    @Id
    @Column(name = "CategoryID", length = 36, nullable = false)
    private String categoryID;

    @Enumerated(EnumType.STRING)
    @Column(name = "CategoryName", length = 100, nullable = false)
    private SurveyCategory categoryName;
} 