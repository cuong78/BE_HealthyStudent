package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Department")
public class Department {

    @Id
    @Column(name = "DepartmentID", length = 36, nullable = false)
    private String departmentID;

    @Column(name = "Name", length = 100, nullable = false)
    private String name;

    @OneToMany(mappedBy = "department")
    private Set<Psychologists> psychologists;

    @OneToMany(mappedBy = "department")
    private Set<Programs> programs;

    public Department(String departmentID, String name) {
        this.departmentID = departmentID;
        this.name = name;
    }
}
