package com.schedule.app.repository;

import com.schedule.app.entities.ScheduleFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IScheduleFileRepository extends JpaRepository<ScheduleFile,Long> {
    @Query("Select sf from ScheduleFile sf where sf.fileStatus='USED'")
    ScheduleFile getUsedScheduleFile();
}
