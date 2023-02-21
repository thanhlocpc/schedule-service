package com.schedule.app.entities;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ThanhLoc
 * @created 2/17/2023
 */
@Entity
@Table(name = "`score_table`")
@Data
public class ScoreTable extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private User user;

    private Double numberScoreFour;

    private Double numberScoreTen;

    private String literalScore;

    private Boolean isPass;
}
