package com.schedule.app.models.dtos.course_registration_result;

import com.schedule.app.entities.CourseTime;
import com.schedule.app.models.dtos.auths.UserDTO;
import com.schedule.app.models.dtos.course.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ThanhLoc
 * @created 2/2/2023
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRegistrationResultDTO implements Serializable {

    private CourseDTO course;

    private CourseTime courseTimePractice;

//    private UserDTO user;

    private Date createdAt;
}
