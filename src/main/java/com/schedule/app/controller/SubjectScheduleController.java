package com.schedule.app.controller;

import com.schedule.app.converter.SubjectScheduleConverter;
import com.schedule.app.entities.SubjectSchedule;
import com.schedule.app.entities.SubjectScheduleResult;
import com.schedule.app.handler.ScheduleServiceException;
import com.schedule.app.models.dtos.SubjectSchedule.SubjectScheduleDTO;
import com.schedule.app.security.UserPrincipal;
import com.schedule.app.utils.Constants;
import com.schedule.app.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ExtensionMethod(Extensions.class)
@RestController
@RequestMapping(Constants.SUBJECT_SCHEDULE_SERVICE_URL)
public class SubjectScheduleController extends BaseAPI {


    @GetMapping("/{academyYear}")
    public List<SubjectScheduleDTO> getSubjectSchedulesByAcademyYear(@PathVariable(name = "academyYear") Integer year) {
        List<SubjectSchedule> subjectSchedules = subjectScheduleService.getSubjectSchedulesByAcademyYear(year);
//        List<SubjectSchedule> subjectSchedules = subjectScheduleService.getSubjectSchedules();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(SubjectSchedule.class, SubjectScheduleDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getSubject().getName(), SubjectScheduleDTO::setSubjectName);
            mapper.map(src -> src.getClassroom().getName(), SubjectScheduleDTO::setClassroomName);
            mapper.map(src -> src.getCourse().getName(), SubjectScheduleDTO::setCourseName);
            mapper.map(src -> src.getCourse().getSemester().getSemesterName(), SubjectScheduleDTO::setSemesterName);
            mapper.map(src -> src.getCourse().getClassEntity().getName(), SubjectScheduleDTO::setClassName);
            mapper.map(src -> src.getCourse().getSemester().getAcademyYear(), SubjectScheduleDTO::setAcademyYear);
        });
        List<SubjectScheduleDTO> subjectScheduleDTO = subjectSchedules.stream().map(subjectSchedule -> modelMapper.map(subjectSchedule, SubjectScheduleDTO.class)).collect(Collectors.toList());
        return subjectScheduleDTO;
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
