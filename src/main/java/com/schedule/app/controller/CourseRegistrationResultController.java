package com.schedule.app.controller;

import com.schedule.app.converter.CourseRegistrationResultConverter;
import com.schedule.app.entities.CourseRegistrationResult;
import com.schedule.app.handler.ScheduleServiceException;
import com.schedule.app.models.dtos.course_registration_result.CourseRegistrationResultDTO;
import com.schedule.app.security.UserPrincipal;
import com.schedule.app.utils.Constants;
import com.schedule.app.utils.Extensions;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.ExtensionMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
public class CourseRegistrationResultController extends BaseAPI{

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
                        .map(e->CourseRegistrationResultConverter.toCourseRegistrationResultDTO(e))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(courseRegistrationResultDTOS);
    }
}
