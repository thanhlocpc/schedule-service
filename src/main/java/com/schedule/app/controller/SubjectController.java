package com.schedule.app.controller;

import com.schedule.app.annotations.swagger.RequiredHeaderToken;
import com.schedule.app.entities.ScheduleFile;
import com.schedule.app.models.dtos.subject.SubjectDTO;
import com.schedule.app.models.wrapper.ObjectResponseWrapper;
import com.schedule.app.requests.GenerateScheduleRequest;
import com.schedule.app.services.IScheduleFileService;
import com.schedule.app.services.ISubjectService;
import com.schedule.app.utils.Constants;
import com.schedule.app.utils.Extensions;
import com.schedule.initialization.gwo.GWO;
import lombok.experimental.ExtensionMethod;
import com.schedule.initialization.models.Schedule;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

@ExtensionMethod(Extensions.class)
@RestController
@RequestMapping(Constants.SUBJECT_SERVICE_URL)
public class SubjectController {
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private IScheduleFileService scheduleFileService;
    @RequiredHeaderToken
    @GetMapping("/schedule/{fileName}")
    public List<SubjectDTO> getSubjectWithFileName(@PathVariable("fileName") String fileName) throws IOException, ClassNotFoundException {
        return subjectService.getSubjectWithDateByFileName(fileName);
    }

    @RequiredHeaderToken
    @GetMapping("/schedule")
    public List<SubjectDTO> getSubjectWithDate(){
        return subjectService.getSubjectWithDate();
    }
    @RequiredHeaderToken
    @GetMapping("/schedule-files")
    public ResponseEntity getAllSchdeduleFile(){
        return ResponseEntity.ok().body(scheduleFileService.getAllScheduleFile());
    }
    @RequiredHeaderToken
    @PostMapping("/newSchedule")
    public ObjectResponseWrapper generateNewSchedule(@RequestBody GenerateScheduleRequest generateScheduleRequest) throws IOException, InterruptedException, CloneNotSupportedException, ClassNotFoundException {
       try {
           System.out.println("properties");
           generateScheduleRequest.getProperties().forEach(System.out::println);
           scheduleFileService.generateNewSchedule(generateScheduleRequest.getProperties());
       }catch (Exception e){
           e.printStackTrace();
           return ObjectResponseWrapper.builder()
                   .status(0)
                   .message("Vui lòng thử lại.")
                   .build();
       }
        return ObjectResponseWrapper.builder()
                .status(1)
                .message("Đã tạo một lịch thi mới")
                .build();
    }

    @RequiredHeaderToken
    @GetMapping("/export-schedule/{fileName}")
    public ResponseEntity exportSchedule(@PathVariable("fileName") String fileName) throws IOException, ClassNotFoundException {

        Schedule schedule=null;


        GWO gwo=new GWO();
        if(fileName.equals("current")){
//            schedule=gwo.convertByteToSchedule();
            ByteArrayInputStream bis=new ByteArrayInputStream(scheduleFileService.getUsedScheduleFile().getFile());
            ObjectInputStream ois = new ObjectInputStream(bis);
            schedule=(Schedule) ois.readObject();
        }else{
            ByteArrayInputStream bis=new ByteArrayInputStream(scheduleFileService.getScheduleFileByFileName(fileName).getFile());
            ObjectInputStream ois = new ObjectInputStream(bis);
            schedule=(Schedule) ois.readObject();
//            schedule=gwo.convertByteToSchedule(scheduleFileService.getScheduleFileByFileName(fileName).getFile());
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
