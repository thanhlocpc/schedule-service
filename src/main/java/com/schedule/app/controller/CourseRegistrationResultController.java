package com.schedule.app.controller;

import com.schedule.app.converter.CourseRegistrationResultConverter;
import com.schedule.app.entities.Course;
import com.schedule.app.entities.CourseRegistrationResult;
import com.schedule.app.entities.Semester;
import com.schedule.app.handler.ScheduleServiceException;
import com.schedule.app.models.dtos.course_registration_result.CourseRegistrationResultDTO;
import com.schedule.app.models.dtos.score.ScoreTableDTO;
import com.schedule.app.models.dtos.score.SemesterTranscriptDTO;
import com.schedule.app.models.dtos.score.SubjectScoreDTO;
import com.schedule.app.security.UserPrincipal;
import com.schedule.app.utils.Constants;
import com.schedule.app.utils.Extensions;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.ExtensionMethod;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ThanhLoc
 * @created 2/2/2023
 */
@ExtensionMethod(Extensions.class)
@RestController
@RequestMapping(Constants.COURSE_REGISTRATION_RESULT_SERVICE_URL)
@Tag(name = "Thời khóa biểu", description = "TimeTable API")
@Validated
@Transactional
public class CourseRegistrationResultController extends BaseAPI {

    @GetMapping("/student")
    public ResponseEntity getTimeTableByStudent(@RequestParam("year") int year,
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

        // lấy ds thời khóa biểu
        List<CourseRegistrationResult> courseRegistrationResults =
                courseRegistrationResultService.
                        getCourseRegistrationResultByStudentIdAndYearAndSemester(userPrincipal.getUserId(), year, semester);
        List<CourseRegistrationResultDTO> courseRegistrationResultDTOS =
                courseRegistrationResults
                        .stream()
                        .map(e -> CourseRegistrationResultConverter.toCourseRegistrationResultDTO(e))
                        .collect(Collectors.toList());
       courseRegistrationResultDTOS.stream().forEach(e->{
            e.getCourse().getCourseTimes().removeIf(item -> e.getCourseTimePractice() != null &&
                    item.getId() != e.getCourseTimePractice().getId() &&
                    item.getType().equals("TH"));
        });

        return ResponseEntity.ok(courseRegistrationResultDTOS);
    }


    @GetMapping("/mark")
    public ResponseEntity getMark(@RequestHeader("Access-Token") String accessToken) {
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
            // lấy bảng điểm
            ScoreTableDTO scoreTableDTO = courseRegistrationResultService.getScoreTableByStudent(userPrincipal.getUserId());
            return ResponseEntity.ok(scoreTableDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }

    @GetMapping("/export-timetable")
    public ResponseEntity exportExcelTimeTableStudent(@RequestParam("year") int year,
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

            Workbook workbook = courseRegistrationResultService.exportTimeTable(userPrincipal.getUserId(), semester, year);
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

    @GetMapping("/export-score-table")
    public ResponseEntity exportExcelScoreTableStudent(@RequestHeader("Access-Token") String accessToken) {
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

            Workbook workbook = courseRegistrationResultService.exportScoreTable(userPrincipal.getUserId());
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
