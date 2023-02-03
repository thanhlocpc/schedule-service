package com.schedule.app.models.dtos.score;

import com.schedule.app.entities.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ThanhLoc
 * @created 2/3/2023
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectScoreDTO {

    private Subject subject;

    private double numberScoreFour;

    private double numberScoreTen;

    private String literalScore;

    private boolean isPass;
}
