package com.schedule.app.controller;

import com.schedule.app.converter.SubjectScheduleConverter;
import com.schedule.app.entities.ScheduleFile;
import com.schedule.app.entities.SubjectScheduleResult;
import com.schedule.app.handler.ScheduleServiceException;
import com.schedule.app.models.dtos.subject_schedule.SubjectScheduleDTO;
import com.schedule.app.models.enums.EnumsConst;
import com.schedule.app.repository.IScheduleFileRepository;
import com.schedule.app.security.UserPrincipal;
import com.schedule.app.services.IScheduleFileService;
import com.schedule.app.utils.Constants;
import com.schedule.app.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import models.ChangeScheduleRequest;
import models.DateSchedule;
import models.Schedule;
import models.SubjectSchedule;
import org.apache.poi.ss.usermodel.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtensionMethod(Extensions.class)
@RestController
@RequestMapping(Constants.SUBJECT_SCHEDULE_SERVICE_URL)
public class SubjectScheduleController extends BaseAPI {
    @Autowired
    IScheduleFileService scheduleFileService;

    @PostMapping("/change")
    public ResponseEntity changeSchedule(@RequestBody List<ChangeScheduleRequest> changeSchedules) throws IOException, ClassNotFoundException, CloneNotSupportedException {
        Schedule scheduleChange = scheduleFileService.changeSchedule(changeSchedules);
        if (scheduleChange == null)
            return ResponseEntity.badRequest().body(null);
        else
            return ResponseEntity.ok(scheduleChange);
    }

    @GetMapping("")
    public List<SubjectScheduleDTO> getSubjectSchedulesByAcademyYear() throws IOException, ClassNotFoundException {
        ScheduleFile scheduleFile = scheduleFileService.getUsedScheduleFile();
        ByteArrayInputStream bis = new ByteArrayInputStream(scheduleFile.getFile());
        ObjectInputStream ois = new ObjectInputStream(bis);
        Schedule schedule = (Schedule) ois.readObject();
        ois.close();
//        List<SubjectSchedule> subjectSchedules = subjectScheduleService.getSubjectSchedulesByAcademyYear(year);
        List<DateSchedule> dateScheduleList = schedule.getDateScheduleList();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(SubjectSchedule.class, SubjectScheduleDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getSubject().getName(), SubjectScheduleDTO::setSubjectName);
            mapper.map(src -> src.getRoom().getRoom().getName(), SubjectScheduleDTO::setClassroomName);
            mapper.map(src -> src.getRoom().getRegistrationClass().getName(), SubjectScheduleDTO::setCourseName);
            mapper.map(src -> "1", SubjectScheduleDTO::setSemesterName);
            mapper.map(src -> src.getRoom().getRegistrationClass().getGrade().getName(), SubjectScheduleDTO::setClassName);
            mapper.map(src -> "2019", SubjectScheduleDTO::setAcademyYear);
            mapper.map(src -> (src.getShift() + 1), SubjectScheduleDTO::setShift);
        });
        List<String> types = Stream.of(EnumsConst.ExamType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        List<SubjectScheduleDTO> subjectScheduleDTOs = new ArrayList<>();
        dateScheduleList.forEach(item -> {
            item.getSubjectSchedules().forEach(ss ->
            {
                SubjectScheduleDTO ssDto = modelMapper.map(ss, SubjectScheduleDTO.class);
                ssDto.setDateExam(LocalDate.parse(item.getDate()));
                ssDto.setExamType(types.get(ss.getSubject().getExamForms()));
                subjectScheduleDTOs.add(ssDto);
            });
        });
        System.out.println("========schedule actual");
        List<DateSchedule> dses = schedule.getDateScheduleList();

        for (int i = 0; i < dses.size(); i++) {
            System.out.println(dses.get(i).toString());
        }
//        List<SubjectScheduleDTO> subjectScheduleDTO = subjectSchedules.stream().map(subjectSchedule -> modelMapper.map(subjectSchedule, SubjectScheduleDTO.class)).collect(Collectors.toList());
        return subjectScheduleDTOs;
    }

    @GetMapping("/student")
    public ResponseEntity getSubjectScheduleByStudent(@RequestParam("year") int year,
                                                      @RequestParam("semester") int semester,
                                                      @RequestHeader("Access-Token") String accessToken) {

        // lấy theo token
        String token = "";
        if (accessToken != null && accessToken.length() > 6) {
            token = accessToken.substring(6);
        }
        UserPrincipal userPrincipal = jwtUtil.getUserFromToken(token);
        if (userPrincipal == null) {
            throw new ScheduleServiceException("Không tìm thấy user này.");
        }

        // lấy ds lịch thi
        List<SubjectScheduleResult> subjectScheduleResults = courseRegisterResultRepository.getSubjectScheduleByStudentIdAndYearAndSemester(userPrincipal.getUserId(), year, semester);
        if (subjectScheduleResults != null && !subjectScheduleResults.isEmpty()) {
            List<SubjectScheduleDTO> subjectScheduleDTO = subjectScheduleResults.stream().map(subjectSchedule -> SubjectScheduleConverter.toSubjectScheduleDTO(subjectSchedule.getSubjectSchedule())).collect(Collectors.toList());
            return ResponseEntity.ok(subjectScheduleDTO);
        }
        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }

    @GetMapping("/export-schedule")
    public ResponseEntity exportExcelScheduleStudent(@RequestParam("year") int year,
                                                     @RequestParam("semester") int semester,
                                                     @RequestHeader("Access-Token") String accessToken) {
        try {
            // lấy theo token
            String token = "";
            if (accessToken != null && accessToken.length() > 6) {
                token = accessToken.substring(6);
            }
            UserPrincipal userPrincipal = jwtUtil.getUserFromToken(token);
            if (userPrincipal == null) {
                throw new ScheduleServiceException("Không tìm thấy user này.");
            }

            Workbook workbook = subjectScheduleService.exportSchedule(userPrincipal.getUserId(), semester, year);
            String fileName = "lich-thi" + ".xlsx";
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition", "attachment;filename=" + fileName)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }


}
