package com.schedule.app.services;

import com.schedule.app.entities.ScheduleFile;
import models.ChangeScheduleRequest;
import models.Schedule;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.util.List;

public interface IScheduleFileService {
    void addScheduleFile(ScheduleFile scheduleFile);
    Schedule generateNewSchedule() throws IOException, InterruptedException, CloneNotSupportedException, ClassNotFoundException;
    ScheduleFile getUsedScheduleFile();

    Schedule changeSchedule(List<ChangeScheduleRequest> changeScheduleRequests) throws IOException, ClassNotFoundException, CloneNotSupportedException;
     Workbook exportSchedule(Long uid, int semester, int year, Schedule schedule);
}
