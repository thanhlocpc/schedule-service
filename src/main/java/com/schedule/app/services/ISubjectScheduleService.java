package com.schedule.app.services;

import com.schedule.app.entities.SubjectSchedule;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public interface ISubjectScheduleService {
    List<SubjectSchedule> getSubjectSchedulesByAcademyYear( int year);
    List<SubjectSchedule> getSubjectSchedules();

    Workbook exportSchedule(Long uid, int semester, int year);
}
