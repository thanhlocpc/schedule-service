package com.schedule.app.controller;

import com.schedule.app.converter.CourseRegistrationResultConverter;
import com.schedule.app.entities.Course;
import com.schedule.app.entities.CourseRegistrationResult;
import com.schedule.app.entities.Semester;
import com.schedule.app.handler.ScheduleServiceException;
import com.schedule.app.models.dtos.course_registration_result.CourseRegistrationResultDTO;
import com.schedule.app.models.dtos.score.SemesterTranscriptDTO;
import com.schedule.app.models.dtos.score.SubjectScoreDTO;
import com.schedule.app.security.UserPrincipal;
import com.schedule.app.utils.Constants;
import com.schedule.app.utils.Extensions;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.ExtensionMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
            List<CourseRegistrationResult> courseRegistrationResults = courseRegistrationResultService.
                    findCourseRegistrationResultByStudent(userPrincipal.getUserId());

            Map<Integer, List<CourseRegistrationResult>> map = new TreeMap<>();
            for (CourseRegistrationResult c : courseRegistrationResults) {
                Integer key = c.getCourse().getSemester().getAcademyYear() * 10 + c.getCourse().getSemester().getSemesterName();
                if (map.get(key) != null) {
                    List<CourseRegistrationResult> courses = map.get(key);
                    courses.add(c);
                } else {
                    List<CourseRegistrationResult> courses = new ArrayList<>();
                    courses.add(c);
                    map.put(key, courses);
                }
            }
            List<SemesterTranscriptDTO> semesterTranscriptDTOS = new ArrayList<>();
            for (Map.Entry<Integer, List<CourseRegistrationResult>> en : map.entrySet()) {
                List<CourseRegistrationResult> courses = en.getValue();
                SemesterTranscriptDTO semesterTranscriptDTO = new SemesterTranscriptDTO();
                semesterTranscriptDTO.setSemester(courses.get(0).getCourse().getSemester());
                List<SubjectScoreDTO> subjectScoreDTOS = new ArrayList<>();
                semesterTranscriptDTO.setSubjects(subjectScoreDTOS);
                double totalScore = 0;
                double avgScore = 0;
                int totalCredit = 0;
                int totalCreditPass = 0;

                for (CourseRegistrationResult c : courses) {
                    totalScore += c.getNumberScoreTen() * c.getCourse().getSubject().getCredit();
                    totalCredit += c.getCourse().getSubject().getCredit();
                    SubjectScoreDTO subjectScoreDTO = new SubjectScoreDTO();
                    if (c.getNumberScoreTen() >= 4) {
                        totalCreditPass += c.getCourse().getSubject().getCredit();
                        subjectScoreDTO.setPass(true);
                    }
                    subjectScoreDTO.setSubject(c.getCourse().getSubject());
                    subjectScoreDTO.setNumberScoreTen(c.getNumberScoreTen());
                    subjectScoreDTO.setNumberScoreFour(c.getNumberScoreFour());
                    subjectScoreDTO.setLiteralScore(c.getLiteralScore());
                    subjectScoreDTOS.add(subjectScoreDTO);
                }
                avgScore = Math.floor(((double) totalScore / totalCredit) * 100) / 100;
                semesterTranscriptDTO.setAvgScore(avgScore);
                semesterTranscriptDTO.setTotalCredit(totalCreditPass);
                semesterTranscriptDTOS.add(semesterTranscriptDTO);
            }

            return ResponseEntity.ok(semesterTranscriptDTOS);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }
}
