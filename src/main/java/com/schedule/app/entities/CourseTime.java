package com.schedule.app.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author ThanhLoc
 * @created 2/2/2023
 */
@Entity
@Table(name = "course_time")
@Data
public class CourseTime extends BaseEntity{

//    @ManyToOne
//    @JoinColumn(name = "course_id", referencedColumnName = "id")
//    private Course course;

    @Column(name = "course_id")
    private Long courseId;

    @ManyToOne
    @JoinColumn(name = "class_room_id", referencedColumnName = "id")
    private Classroom classroom;

    private int dayOfWeek;

    private int timeStart;

    private Date createdAt;
}
