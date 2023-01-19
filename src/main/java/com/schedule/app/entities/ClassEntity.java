package com.schedule.app.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "class")
@Data
public class ClassEntity extends BaseEntity{
    private String name;
}
