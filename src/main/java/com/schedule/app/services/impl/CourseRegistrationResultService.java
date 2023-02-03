package com.schedule.app.services.impl;

import com.schedule.app.entities.CourseRegistrationResult;
import com.schedule.app.services.ABaseServices;
import com.schedule.app.services.ICourseRegistrationResultService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ThanhLoc
 * @created 2/2/2023
 */
@Service
public class CourseRegistrationResultService extends ABaseServices implements ICourseRegistrationResultService {
    @Override
    public List<CourseRegistrationResult> getCourseRegistrationResultByStudentIdAndYearAndSemester(Long studentId, int year, int semester) {
        return courseRegistrationResultRepository.findCourseRegistrationResultByStudentAndSemester(studentId,year,semester);
    }

    @Override
    public List<CourseRegistrationResult> findCourseRegistrationResultByStudent(Long studentId) {
        return courseRegistrationResultRepository.findCourseRegistrationResultByStudent(studentId);
    }
}
