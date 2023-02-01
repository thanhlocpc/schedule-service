package com.schedule.app.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author ThanhLoc
 * @created 1/26/2023
 */
@Entity
@Table(name = "`subject_schedule_result`")
@Data
public class SubjectScheduleResult extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "subject_schedule_id", referencedColumnName = "id")
    private SubjectSchedule subjectSchedule;

    private Long studentId;

    private Double mark;

    private Date createdAt;
}
