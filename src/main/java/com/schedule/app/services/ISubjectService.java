package com.schedule.app.services;

import com.schedule.app.models.dtos.subject.SubjectDTO;

import java.util.List;

public interface ISubjectService {
    List<SubjectDTO> getSubjectWithDate();
}
