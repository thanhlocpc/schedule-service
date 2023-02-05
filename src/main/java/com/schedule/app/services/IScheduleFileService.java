package com.schedule.app.services;

import com.schedule.app.entities.ScheduleFile;
import models.ChangeScheduleRequest;
import models.Schedule;

import java.io.IOException;
import java.util.List;

public interface IScheduleFileService {
    void addScheduleFile(ScheduleFile scheduleFile);
    void generateNewSchedule() throws IOException, InterruptedException, CloneNotSupportedException;
    ScheduleFile getUsedScheduleFile();

    Schedule changeSchedule(List<ChangeScheduleRequest> changeScheduleRequests) throws IOException, ClassNotFoundException, CloneNotSupportedException;
}
