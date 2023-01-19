package com.schedule.app.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
}
