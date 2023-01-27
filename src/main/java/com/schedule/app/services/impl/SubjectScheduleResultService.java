package com.schedule.app.services.impl;

import com.schedule.app.entities.SubjectScheduleResult;
import com.schedule.app.services.ABaseServices;
import com.schedule.app.services.ISubjectScheduleResultService;

import java.util.List;

/**
 * @author ThanhLoc
 * @created 1/27/2023
 */
public class SubjectScheduleResultService extends ABaseServices implements ISubjectScheduleResultService {
    @Override
    public List<SubjectScheduleResult> getSubjectScheduleByStudentIdAndYearAndSemester(Long studentId, int year, int semester) {
        return subjectScheduleResultRepository.getSubjectScheduleByStudentIdAndYearAndSemester(studentId, year, semester);
    }
}
