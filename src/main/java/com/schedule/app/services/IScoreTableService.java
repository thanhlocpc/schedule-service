package com.schedule.app.services;

import com.schedule.app.entities.ScoreTable;
import com.schedule.app.models.dtos.score.ScoreTableDTO;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * @author : Thành Lộc
 * @since : 10/12/2022, Wed
 **/

public interface IScoreTableService {
    public ScoreTableDTO findScoreTableByStudent(Long studentId);

    Workbook exportScoreTable(Long userId);


}
