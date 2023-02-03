package com.schedule.app.services;

import com.schedule.app.entities.CourseRegistrationResult;

import java.util.List;

/**
 * @author ThanhLoc
 * @created 2/2/2023
 */
public interface ICourseRegistrationResultService {

    public List<CourseRegistrationResult> getCourseRegistrationResultByStudentIdAndYearAndSemester(Long studentId,
                                                                                                   int year,
                                                                                                   int semester);
    public List<CourseRegistrationResult> findCourseRegistrationResultByStudent(Long studentId);
}
