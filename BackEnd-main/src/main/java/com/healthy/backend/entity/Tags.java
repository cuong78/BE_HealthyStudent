package com.healthy.backend.entity;

import com.healthy.backend.enums.ProgramTags;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Tags")
public class Tags {

    @Id
    @Column(name = "TagID", length = 36, nullable = false)
    private String tagId;

    @Column(name = "TagName", length = 100, nullable = false)
    private String tagName;

    @ManyToMany(mappedBy = "tags")
    private Set<Programs> programs;

    public Tags(String tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public Tags(String tagId, ProgramTags tagName) {
        this.tagId = tagId;
        this.tagName = tagName.toString().replace("_", " ");
    }
}