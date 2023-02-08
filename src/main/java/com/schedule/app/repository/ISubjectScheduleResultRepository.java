package com.schedule.app.repository;

import com.schedule.app.entities.SubjectScheduleResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author ThanhLoc
 * @created 1/26/2023
 */
public interface ISubjectScheduleResultRepository extends JpaRepository<SubjectScheduleResult, Long> {


    @Query("select distinct c from SubjectScheduleResult c join SubjectSchedule ss on c.subjectSchedule.course.id = ss.course.id join Semester s on " +
            "s.academyYear = :year and s.semesterName = :semester where c.userId = :userId")
    List<SubjectScheduleResult> getSubjectScheduleByUserIdAndYearAndSemester(@Param("userId") Long userId, @Param("year") int year,
                                                                                @Param("semester") int semester);
}
