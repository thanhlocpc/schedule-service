package com.schedule.app.controller;

import com.schedule.app.models.dtos.subject.SubjectDTO;
import com.schedule.app.services.IScheduleFileService;
import com.schedule.app.services.ISubjectService;
import com.schedule.app.utils.Constants;
import com.schedule.app.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@ExtensionMethod(Extensions.class)
@RestController
@RequestMapping(Constants.SUBJECT_SERVICE_URL)
public class SubjectController {
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private IScheduleFileService scheduleFileService;
    @GetMapping("/schedule")
    public List<SubjectDTO> getSubjectWithDate() throws IOException, ClassNotFoundException {
        return subjectService.getSubjectWithDate();
    }

    @PostMapping("/newSchedule")
    public void generateNewSchedule() throws IOException, InterruptedException, CloneNotSupportedException {
        scheduleFileService.generateNewSchedule();
    }
}
