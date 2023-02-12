package com.schedule.app.repository;

import com.schedule.app.entities.Semester;
import com.schedule.app.entities.SubjectSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISubjectScheduleRepository extends JpaRepository<SubjectSchedule,Long> {
    @Query("Select ss from SubjectSchedule ss where ss.course.semester.academyYear=:year")
    List<SubjectSchedule> getSubjectSchedulesByAcademyYear(@Param("year") int year);

    @Query("select ss from SubjectSchedule ss where ss.course.id in :courseIds")
    List<SubjectSchedule> getAllSubjectScheduleByCourse_Id(@Param("courseIds") List<Long> courseIds);
    @Modifying
    @Query("delete from SubjectSchedule ss where ss.course in (select c from Course c where c.semester=:semester)")
    void deleteSubjectScheduleBySemester(@Param("semester") Semester semester);
}
