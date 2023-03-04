package com.schedule.app.services;

import com.schedule.app.entities.ScheduleFile;
import com.schedule.app.models.dtos.schedule_file.ScheduleFileDTO;
import com.schedule.app.models.enums.FileStatus;
import com.schedule.initialization.models.ChangeScheduleRequest;
import com.schedule.initialization.models.ChangeSubjectScheduleRequest;
import com.schedule.initialization.models.Schedule;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.util.List;

public interface IScheduleFileService {
    void addScheduleFile(ScheduleFile scheduleFile);
    Schedule generateNewSchedule(List<Integer> properties) throws IOException, InterruptedException, CloneNotSupportedException, ClassNotFoundException;
    ScheduleFile getUsedScheduleFile();
    ScheduleFile getScheduleFileByFileName(String fileName);
    List<ScheduleFileDTO> getAllScheduleFile();
    Schedule changeSchedule(List<ChangeScheduleRequest> changeScheduleRequests) throws IOException, ClassNotFoundException, CloneNotSupportedException;
    Schedule changeSubjectSchedule(List<ChangeSubjectScheduleRequest> changeScheduleRequests)throws IOException, ClassNotFoundException, CloneNotSupportedException;
    Workbook exportSchedule(Long uid, int semester, int year, Schedule schedule);

    void setFileUsedToStatus(FileStatus fileStatus);
}
