package com.schedule.app.models.dtos.subject_schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ThanhLoc
 * @created 3/4/2023
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleItemDTO {

    private int shift;

    private List<SubjectScheduleDTO> subjectSchedules;
}
