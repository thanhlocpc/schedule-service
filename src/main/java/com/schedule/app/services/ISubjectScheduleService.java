package com.schedule.app.services;

import com.schedule.app.entities.SubjectSchedule;

import java.util.List;

public interface ISubjectScheduleService {
    List<SubjectSchedule> getSubjectSchedulesByAcademyYear( int year);
    List<SubjectSchedule> getSubjectSchedules();
}
