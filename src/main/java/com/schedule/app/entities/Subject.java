package com.schedule.app.entities;

//import com.schedule.app.models.enums.ExamType;
import com.schedule.app.models.enums.EnumsConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "subject")
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "lesson_time")
    private int lessonTime;

    private int credit;
}
