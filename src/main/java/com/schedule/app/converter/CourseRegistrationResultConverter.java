package com.schedule.app.converter;

import com.schedule.app.entities.CourseRegistrationResult;
import com.schedule.app.models.dtos.course_registration_result.CourseRegistrationResultDTO;
import org.modelmapper.ModelMapper;

/**
 * @author ThanhLoc
 * @created 2/2/2023
 */
public class CourseRegistrationResultConverter {

    public static CourseRegistrationResultDTO toCourseRegistrationResultDTO(CourseRegistrationResult course){
        CourseRegistrationResultDTO res = CourseRegistrationResultDTO
                .builder()
//                .user(UserConverter.toUserDTO(course.getUser()))
                .course(CourseConverter.toCourseDTO(course.getCourse()))
                .courseTimePractice(course.getCourseTimePractice())
                .build();
        return res;
    }
}
