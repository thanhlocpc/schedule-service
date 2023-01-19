package com.schedule.app.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "course")
@Data
public class Course extends BaseEntity{
    @Column(nullable = false)
    @NotNull
    private String name;

    private int estimatedClassSize;

    @Column(nullable = false)
    @NotNull
    private int realClassSize;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;
}
