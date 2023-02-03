package com.schedule.app.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author ThanhLoc
 * @created 2/2/2023
 */
@Entity
@Table(name = "course_registration_result")
@Data
public class CourseRegistrationResult extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private User user;

    private double numberScoreFour;

    private double numberScoreTen;

    private String literalScore;

    private boolean isPass;

    private Date createdAt;
}
