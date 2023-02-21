package com.schedule.app.controller;

import com.schedule.app.entities.ScheduleFile;
import com.schedule.app.models.dtos.subject.SubjectDTO;
import com.schedule.app.services.IScheduleFileService;
import com.schedule.app.services.ISubjectService;
import com.schedule.app.utils.Constants;
import com.schedule.app.utils.Extensions;
import gwo.GWO;
import lombok.experimental.ExtensionMethod;
import models.Schedule;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
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
    @GetMapping("/schedule/{fileName}")
    public List<SubjectDTO> getSubjectWithFileName(@PathVariable("fileName") String fileName) throws IOException, ClassNotFoundException {
        return subjectService.getSubjectWithDateByFileName(fileName);
    }
    @GetMapping("/schedule")
    public List<SubjectDTO> getSubjectWithDate(){
        return subjectService.getSubjectWithDate();
    }
    @GetMapping("/schedule-files")
    public ResponseEntity getAllSchdeduleFile(){
        return ResponseEntity.ok().body(scheduleFileService.getAllScheduleFile());
    }
    @PostMapping("/newSchedule")
    public ResponseEntity generateNewSchedule() throws IOException, InterruptedException, CloneNotSupportedException, ClassNotFoundException {
       Schedule schedule= scheduleFileService.generateNewSchedule();

        return ResponseEntity.ok()
                .build();
    }
    @GetMapping("/export-schedule/{fileName}")
    public ResponseEntity exportSchedule(@PathVariable("fileName") String fileName) throws IOException, ClassNotFoundException {

        Schedule schedule=null;
        GWO gwo=new GWO();
        if(fileName.equals("current")){
            schedule=gwo.convertByteToSchedule(scheduleFileService.getUsedScheduleFile().getFile());
        }else{
            schedule=gwo.convertByteToSchedule(scheduleFileService.getScheduleFileByFileName(fileName).getFile());
        }
        Workbook workbook = scheduleFileService.exportSchedule(1L, 1,2020,schedule);
        String fileExport = "lich-thi" + ".xlsx";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", "attachment;filename=" + fileExport)
                .body(outputStream.toByteArray());
    }
}
