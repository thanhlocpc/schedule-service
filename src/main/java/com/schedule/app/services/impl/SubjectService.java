package com.schedule.app.services.impl;

import com.schedule.app.entities.Subject;
import com.schedule.app.models.dtos.Subject.SubjectDTO;
import com.schedule.app.repository.ISubjectRepository;
import com.schedule.app.services.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SubjectService implements ISubjectService {
    @Autowired
    ISubjectRepository subjectRepository;
    @Override
    public List<SubjectDTO> getSubjectWithDate() {
        return subjectRepository.getSubjectWithDate();
    }
}
