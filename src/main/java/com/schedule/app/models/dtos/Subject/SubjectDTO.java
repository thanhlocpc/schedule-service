package com.schedule.app.models.dtos.Subject;

import com.schedule.app.entities.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDTO {
    private Subject subject;
    private LocalDate date;
}
