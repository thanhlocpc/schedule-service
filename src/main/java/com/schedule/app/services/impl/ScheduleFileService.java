package com.schedule.app.services.impl;

import com.schedule.app.entities.ScheduleFile;
import com.schedule.app.models.enums.FileStatus;
import com.schedule.app.repository.IScheduleFileRepository;
import com.schedule.app.services.IScheduleFileService;
import gwo.GWO;
import models.ChangeScheduleRequest;
import models.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleFileService implements IScheduleFileService {
    @Autowired
    IScheduleFileRepository scheduleFileRepository;

    @Override
    public void addScheduleFile(ScheduleFile scheduleFile) {
        scheduleFileRepository.save(scheduleFile);
    }

    @Override
    public void generateNewSchedule() throws IOException, InterruptedException, CloneNotSupportedException {
        List<String> dates = new ArrayList<>();
        dates.add("2022-10-12");
        dates.add("2022-10-13");
        dates.add("2022-10-14");
        dates.add("2022-10-15");
        dates.add("2022-10-16");
        dates.add("2022-10-17");
        dates.add("2022-10-18");
        dates.add("2022-10-19");
        dates.add("2022-10-20");
        GWO gwo = new GWO(dates);
        byte[] scheduleByteArray = gwo.generateNewSchedule(1);
        addScheduleFile(new ScheduleFile(scheduleByteArray, FileStatus.NEW));
    }

    @Override
    public ScheduleFile getUsedScheduleFile() {
        return scheduleFileRepository.getUsedScheduleFile();
    }

    @Override
    public Schedule changeSchedule(List<ChangeScheduleRequest> changeScheduleRequests) throws IOException, ClassNotFoundException, CloneNotSupportedException {
        ScheduleFile scheduleFile = getUsedScheduleFile();

        ByteArrayInputStream bis = new ByteArrayInputStream(scheduleFile.getFile());
        ObjectInputStream ois = new ObjectInputStream(bis);
        Schedule schedule = (Schedule) ois.readObject();
        ois.close();
        GWO gwo = new GWO();
        byte[] scheduleAfterChangeByteArray = gwo.changeSchedule(changeScheduleRequests, schedule);
        Schedule scheduleAfterChange = null;
        if (scheduleAfterChangeByteArray.length > 0) {
            ByteArrayInputStream bisAC = new ByteArrayInputStream(scheduleAfterChangeByteArray);
            ObjectInputStream oisAC = new ObjectInputStream(bisAC);
            scheduleAfterChange = (Schedule) oisAC.readObject();
            ois.close();
        }

        return scheduleAfterChange;
    }


}
