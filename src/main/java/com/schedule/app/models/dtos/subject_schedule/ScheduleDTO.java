package com.schedule.app.models.dtos.subject_schedule;

import com.schedule.initialization.models.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ThanhLoc
 * @created 3/4/2023
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {

    private String date;

    private ScheduleItemDTO scheduleItem;
}
