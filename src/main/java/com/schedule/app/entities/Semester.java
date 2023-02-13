package com.schedule.app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "semester")
@Data
@NotNull
@AllArgsConstructor
@NoArgsConstructor
public class Semester extends  BaseEntity{
    @Column(nullable = false)
    private int semesterName;

    @Column(nullable = false)
    private int academyYear;

}
