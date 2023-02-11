package com.schedule.app.entities;

import com.schedule.app.models.enums.FileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "schedule_file")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleFile extends BaseEntity{
    @Lob
    private byte[] file;
    @Enumerated(EnumType.STRING)
    private FileStatus fileStatus;
}