package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Parents")
public class Parents {

    @Id
    @Column(name = "ParentID", length = 36, nullable = false)
    private String parentID;

    @Column(name = "UserID", length = 36, nullable = false)
    private String userID;

    @OneToMany(mappedBy = "parents", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Students> students;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Users user;

    public Parents(String parentID, String userID) {
        this.parentID = parentID;
        this.userID = userID;
    }
} 