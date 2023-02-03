package com.schedule.app.models.dtos.score;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ThanhLoc
 * @created 2/3/2023
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreTableDTO {

    List<SemesterTranscriptDTO> semesterTranscripts;

    private int totalCredit;

    private double avgScoreTen;

    private double avgScoreFour;
}
