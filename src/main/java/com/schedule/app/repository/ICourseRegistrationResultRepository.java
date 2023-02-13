package com.schedule.app.repository;

import com.schedule.app.entities.CourseRegistrationResult;
import com.schedule.app.entities.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("select distinct c from CourseRegistrationResult c where c.user.id = :studentId")
    public List<CourseRegistrationResult> findCourseRegistrationResultByStudent(@Param("studentId") Long studentId);

    @Modifying
    @Query("delete from CourseRegistrationResult crr where crr.course in (select c from Course c where c.semester=:semester)")
    void deleteCourseRegistrationResultBySemester(@Param("semester") Semester semester);
}
