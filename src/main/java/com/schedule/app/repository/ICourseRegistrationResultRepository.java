package com.schedule.app.repository;

import com.schedule.app.entities.CourseRegistrationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author ThanhLoc
 * @created 2/2/2023
 */
public interface ICourseRegistrationResultRepository extends JpaRepository<CourseRegistrationResult, Long> {

    @Query("select distinct c from CourseRegistrationResult c where c.user.id = :studentId and c.course.semester.academyYear =:year " +
            "and c.course.semester.semesterName =:semester")
    public List<CourseRegistrationResult> findCourseRegistrationResultByStudentAndSemester(@Param("studentId") Long studentId,
                                                                                           @Param("year") int year,
                                                                                           @Param("semester") int semester);
}
