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

    @Column(name = "course_time_practices")
    private String courseTimePractices;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private Double numberScoreFour;

    private Double numberScoreTen;

    private String literalScore;

    private Boolean isPass;

    private Date createdAt;
}
