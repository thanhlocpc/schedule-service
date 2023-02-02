package com.schedule.app.converter;

import com.schedule.app.entities.Course;
import com.schedule.app.entities.User;
import com.schedule.app.models.dtos.auths.UserDTO;
import com.schedule.app.models.dtos.course.CourseDTO;
import org.modelmapper.ModelMapper;

/**
 * @author ThanhLoc
 * @created 2/2/2023
 */
public class CourseConverter {

    public static CourseDTO toCourseDTO(Course course){
        ModelMapper modelMapper = new ModelMapper();
        CourseDTO res = modelMapper.map(course, CourseDTO.class);
        return res;
    }

}
