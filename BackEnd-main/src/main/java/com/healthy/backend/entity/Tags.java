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

    public Tags(String tagId, Tag tagName) {
        this.tagId = tagId;
        this.tagName = tagName.toString().replace("_", " ");
    }
    public enum Tag {
        Stress,
        Anxiety,
        Mindfulness,
        Depression,
        EatingDisorder,
        Addiction,
        Self_Care,
        Sleep,
        PhysicalHealth,
        MentalHealth,
        Peer_Support,
        Community,
        Social_Skill,
        Exercise,
        Relaxation, 
        Motivation,
        Meditation,
        Test_Anxiety,
        Test_Depression,
        Test_Stress,
        Academic_Stress,
        Work_Stress,
        Financial_Stress,
        Relationship_Stress,
        Self_Improvement_Stress,
        Academic_Relationship,
        Work_Relationship,
        Financial_Relationship,
        Relationship,
        Self_Improvement,
        Performance,
        Confidence,
        Self_Esteem,
        Self_Awareness,
        Self_Discipline,
        Self_Reflection,
        Self_Management,
        Resilience,
        Coping_Skills,
        Problem_Solving,
        Decision_Making,
        Time_Management,
        Stress_Management,
        Emotional_Intelligence,
        Emotional_Regulation,
        Emotional_Expression,
        Productivity,
        Boundaries,
        Self_Control, Wellness, Health, Grief, Support, Healing, Body_Image,
        Personal_Development,
        Self_Acceptance,
        Self_Health,
        Social_Support,
        Social_Connectivity,
        Social_Interaction,
        Community_Engagement,
        Community_Involvement,
        Relationship_Building,
        Relationship_Health
    }

}