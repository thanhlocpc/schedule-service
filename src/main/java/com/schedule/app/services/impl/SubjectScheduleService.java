package com.schedule.app.services.impl;

import com.schedule.app.entities.SubjectSchedule;
import com.schedule.app.repository.ISubjectScheduleRepository;
import com.schedule.app.services.ISubjectScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SubjectScheduleService implements ISubjectScheduleService {
    @Autowired
    ISubjectScheduleRepository subjectScheduleRepository;
    @Override
    public List<SubjectSchedule> getSubjectSchedulesByAcademyYear(int year) {
        return subjectScheduleRepository.getSubjectSchedulesByAcademyYear(year);
    }

    @Override
    public List<SubjectSchedule> getSubjectSchedules() {
        return subjectScheduleRepository.findAll();
    }
}
