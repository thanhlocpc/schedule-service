package com.schedule.app.services;

import com.schedule.app.entities.SubjectScheduleResult;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author ThanhLoc
 * @created 1/27/2023
 */
public interface ISubjectScheduleResultService{

    List<SubjectScheduleResult> getSubjectScheduleByUserIdAndYearAndSemester(Long studentId, int year, int semester);

}
