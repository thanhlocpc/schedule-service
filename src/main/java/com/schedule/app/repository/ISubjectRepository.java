package com.schedule.app.repository;

import com.schedule.app.entities.Subject;
import com.schedule.app.models.dtos.subject.SubjectDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ISubjectRepository extends JpaRepository<Subject,Long> {
    @Query(value = "SELECT DISTINCT new com.schedule.app.models.dtos.subject.SubjectDTO(ss.subject,ss.dateExam) from SubjectSchedule ss")
    List<SubjectDTO> getSubjectWithDate();
}
