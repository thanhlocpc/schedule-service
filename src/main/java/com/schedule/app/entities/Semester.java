package com.schedule.app.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "semester")
@Data
@NotNull
public class Semester extends  BaseEntity{
    @Column(nullable = false)
    private String semesterName;

    @Column(nullable = false)
    private int academyYear;

}
