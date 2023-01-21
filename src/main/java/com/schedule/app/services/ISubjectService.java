package com.schedule.app.services;

import com.schedule.app.entities.Subject;
import com.schedule.app.models.dtos.Subject.SubjectDTO;

import java.util.List;

public interface ISubjectService {
    List<SubjectDTO> getSubjectWithDate();
}
