package com.schedule.app.repository;

import com.schedule.app.entities.SubjectSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISubjectScheduleRepository extends JpaRepository<SubjectSchedule,Long> {
    @Query("Select ss from SubjectSchedule ss where ss.course.semester.academyYear=:year")
    List<SubjectSchedule> getSubjectSchedulesByAcademyYear(@Param("year") int year);
}
