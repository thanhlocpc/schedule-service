package com.schedule.app.services.impl;

import com.schedule.app.entities.ScheduleFile;
import com.schedule.app.entities.Subject;
import com.schedule.app.models.dtos.subject.SubjectDTO;
import com.schedule.app.models.enums.EnumsConst;
import com.schedule.app.repository.IScheduleFileRepository;
import com.schedule.app.repository.ISubjectRepository;
import com.schedule.app.services.ISubjectService;
import com.schedule.initialization.models.DateSchedule;
import com.schedule.initialization.models.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService implements ISubjectService {
    @Autowired
    ISubjectRepository subjectRepository;
    @Autowired
    IScheduleFileRepository scheduleFileRepository;

    @Override
    public List<SubjectDTO> getSubjectWithDate(){
        return subjectRepository.getSubjectWithDate();
    }
    @Override
    public List<SubjectDTO> getSubjectWithDateByFileName(String fileName) throws IOException, ClassNotFoundException {
        ScheduleFile scheduleFile=scheduleFileRepository.getScheduleFileByName(fileName);
        ByteArrayInputStream bis=new ByteArrayInputStream(scheduleFile.getFile());
        ObjectInputStream ois = new ObjectInputStream(bis);
        Schedule schedule= (Schedule) ois.readObject();
        ois.close();
        List<SubjectDTO> result=new ArrayList<>();
        List<DateSchedule> dateScheduleList=schedule.getDateScheduleList();
        for (DateSchedule ds:dateScheduleList){
            result.addAll( ds.getSubjectSchedules().stream().map(item->item.getSubject()).distinct().map(item->new SubjectDTO(new Subject(item.getId(),item.getName(), null,0,0,0), LocalDate.parse(ds.getDate()))).collect(Collectors.toList()));
        }
//        return subjectRepository.getSubjectWithDate();
        return result;

    }
}
