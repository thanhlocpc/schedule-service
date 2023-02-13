package com.schedule.app.models.dtos.schedule_file;

import com.schedule.app.models.enums.FileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleFileDTO {
    private String name;
    @Enumerated(EnumType.STRING)
    private FileStatus fileStatus;
}
