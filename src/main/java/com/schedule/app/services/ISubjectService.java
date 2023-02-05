package com.schedule.app.services;

import com.schedule.app.models.dtos.subject.SubjectDTO;

import java.io.IOException;
import java.util.List;

public interface ISubjectService {
    List<SubjectDTO> getSubjectWithDate() throws IOException, ClassNotFoundException;
}
