package com.schedule.app.controller;

import com.schedule.app.models.dtos.subject.SubjectDTO;
import com.schedule.app.services.IScheduleFileService;
import com.schedule.app.services.ISubjectService;
import com.schedule.app.utils.Constants;
import com.schedule.app.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import models.Schedule;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
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
    public ResponseEntity generateNewSchedule() throws IOException, InterruptedException, CloneNotSupportedException, ClassNotFoundException {
       Schedule schedule= scheduleFileService.generateNewSchedule();
        Workbook workbook = scheduleFileService.exportSchedule(1L, 1,2020,schedule);
        String fileName = "lich-thi" + ".xlsx";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", "attachment;filename=" + fileName)
                .body(outputStream.toByteArray());
    }
}
