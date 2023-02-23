package com.schedule.app.converter;

import com.schedule.app.entities.SubjectSchedule;
import com.schedule.app.models.dtos.subject_schedule.SubjectScheduleDTO;

/**
 * @author ThanhLoc
 * @created 1/27/2023
 */
public class SubjectScheduleConverter {

    public static SubjectScheduleDTO toSubjectScheduleDTO(SubjectSchedule subjectSchedule){
        SubjectScheduleDTO subjectScheduleDTO = new SubjectScheduleDTO();
        subjectScheduleDTO.setSubjectId(subjectSchedule.getSubject().getId());
        subjectScheduleDTO.setExamType(subjectSchedule.getSubject().getExamType().getDescription());
        subjectScheduleDTO.setSubjectName(subjectSchedule.getSubject().getName());
        subjectScheduleDTO.setCourseName(subjectSchedule.getCourse().getName());
        subjectScheduleDTO.setShift(subjectSchedule.getShift());
        subjectScheduleDTO.setClassroomName(subjectSchedule.getClassroom().getName());
        subjectScheduleDTO.setDateExam(subjectSchedule.getDateExam());
        subjectScheduleDTO.setCandidateAmount(subjectScheduleDTO.getCandidateAmount());
        subjectScheduleDTO.setSubjectScheduleIndex(subjectScheduleDTO.getSubjectScheduleIndex());
        subjectScheduleDTO.setLessonEnd(subjectSchedule.getSubject().getExamTime());
        subjectScheduleDTO.setAcademyYear(subjectSchedule.getCourse().getSemester().getAcademyYear());
        if (subjectSchedule.getShift() == 0) {
            subjectScheduleDTO.setLessonStart(1);
        } else if (subjectSchedule.getShift() == 1) {
            subjectScheduleDTO.setLessonStart(4);
        } else if (subjectSchedule.getShift() == 2) {
            subjectScheduleDTO.setLessonStart(7);
        } else {
            subjectScheduleDTO.setLessonStart(10);
        }

        return subjectScheduleDTO;
    }
}
