package com.schedule.app.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "course")
@Data
public class Course extends BaseEntity{
    @Column(nullable = false)
    @NotNull
    private String name;

    private Integer estimatedClassSize;

    @Column(nullable = false)
    @NotNull
    private Integer realClassSize;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @OneToMany
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private List<CourseTime> courseTimes;
}
