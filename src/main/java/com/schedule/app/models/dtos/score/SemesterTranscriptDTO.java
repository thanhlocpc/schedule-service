package com.schedule.app.models.dtos.score;

import com.schedule.app.entities.Semester;
import com.schedule.app.entities.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ThanhLoc
 * @created 2/3/2023
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SemesterTranscriptDTO implements Serializable {

    private Semester semester;

    private List<SubjectScoreDTO> subjects;

    private int totalCredit;

    private double avgScore;

}
