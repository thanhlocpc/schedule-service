package com.schedule.app.models.dtos.subject_schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectScheduleDTO {

    private String subjectId;

    private String subjectName;

    private String classroomName;

    private String courseName;

    private String semesterName;

    private String className;

    private int academyYear;

    private int shift;

    private int lessonStart;

    private int lessonEnd;

    private String examType;

    private LocalDate dateExam;

    private int subjectScheduleIndex;

    private int candidateAmount;

}
