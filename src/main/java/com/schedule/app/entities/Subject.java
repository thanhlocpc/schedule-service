package com.schedule.app.entities;

//import com.schedule.app.models.enums.ExamType;
import com.schedule.app.models.enums.EnumsConst;
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
    private EnumsConst.ExamType examType;

    @Column(name = "exam_time")
    private int examTime;

    private int credit;
}
