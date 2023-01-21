package com.schedule.app.entities;

import com.schedule.app.models.enums.ClassroomType;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "classroom")
@Data
public class Classroom extends BaseEntity{
    @Column(nullable = false)
    @NotNull
    private String name;

    private int capacityBase;

    @Column(nullable = false)
    @NotNull
    private int capacityExam;
    @Enumerated(EnumType.STRING)
    private ClassroomType classroomType;
}
