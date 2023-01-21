package com.schedule.app.entities;

import com.schedule.app.models.enums.ExamType;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "subject")
@Data
public class Subject {

    @Id
    private String id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private ExamType examType;

    private int credit;
}
