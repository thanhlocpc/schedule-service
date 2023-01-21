package com.schedule.app.controller;

import com.schedule.app.entities.SubjectSchedule;
import com.schedule.app.models.dtos.SubjectSchedule.SubjectScheduleDTO;
import com.schedule.app.services.ISubjectScheduleService;
import com.schedule.app.utils.Constants;
import com.schedule.app.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import javax.xml.ws.Response;
import java.util.List;
import java.util.stream.Collectors;

@ExtensionMethod(Extensions.class)
@RestController
@RequestMapping(Constants.SUBJECT_SCHEDULE_SERVICE_URL)
public class SubjectScheduleController {
    @Autowired
    private ISubjectScheduleService subjectScheduleService;

    @GetMapping("/{academyYear}")
    public List<SubjectScheduleDTO> getSubjectSchedulesByAcademyYear(@PathVariable(name = "academyYear") Integer year) {
        List<SubjectSchedule>subjectSchedules=  subjectScheduleService.getSubjectSchedulesByAcademyYear(year);
//        List<SubjectSchedule> subjectSchedules = subjectScheduleService.getSubjectSchedules();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(SubjectSchedule.class, SubjectScheduleDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getSubject().getName(), SubjectScheduleDTO::setSubjectName);
                    mapper.map(src -> src.getClassroom().getName(), SubjectScheduleDTO::setClassroomName);
                    mapper.map(src -> src.getCourse().getName(), SubjectScheduleDTO::setCourseName);
                    mapper.map(src -> src.getCourse().getSemester().getSemesterName(), SubjectScheduleDTO::setSemesterName);
                    mapper.map(src -> src.getCourse().getClassEntity().getName(), SubjectScheduleDTO::setClassName);
                    mapper.map(src -> src.getCourse().getSemester().getAcademyYear(), SubjectScheduleDTO::setAcademyYear);
                });
        List<SubjectScheduleDTO> subjectScheduleDTO = subjectSchedules
                .stream()
                .map(subjectSchedule -> modelMapper.map(subjectSchedule, SubjectScheduleDTO.class))
                .collect(Collectors.toList());
        return subjectScheduleDTO;
    }


}
