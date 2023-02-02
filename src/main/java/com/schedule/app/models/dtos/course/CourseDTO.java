package com.schedule.app.models.dtos.course;

import com.schedule.app.entities.ClassEntity;
import com.schedule.app.entities.CourseTime;
import com.schedule.app.entities.Semester;
import com.schedule.app.entities.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author ThanhLoc
 * @created 2/2/2023
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO implements Serializable {

    private Long id;

    private String name;

    private int estimatedClassSize;

    private int realClassSize;

    private Subject subject;

    private ClassEntity classEntity;

    private Semester semester;

    private List<CourseTime> courseTimes;

}
