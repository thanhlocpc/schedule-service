package com.schedule.app.controller;

import com.schedule.app.handler.ScheduleServiceException;
import com.schedule.app.models.dtos.score.ScoreTableDTO;
import com.schedule.app.security.UserPrincipal;
import com.schedule.app.utils.Constants;
import com.schedule.app.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.Collections;

/**
 * @author ThanhLoc
 * @created 2/17/2023
 */
@ExtensionMethod(Extensions.class)
@RestController
@RequestMapping(Constants.SCORE_TABLE_SERVICE_URL)
public class ScoreTableController extends BaseAPI{

    @GetMapping("/")
    public ResponseEntity getMark(@RequestHeader("Access-Token") String accessToken) {
        try {
            // lấy theo token
            String token = "";
            if (accessToken != null && accessToken.length() > 6) {
                token = accessToken.substring(6);
            }
            UserPrincipal userPrincipal = jwtUtil.getUserFromToken(token);
            if (userPrincipal == null) {
                throw new ScheduleServiceException("Không tìm thấy user này.");
            }
            // lấy bảng điểm
            ScoreTableDTO scoreTableDTO = scoreTableService.findScoreTableByStudent(userPrincipal.getUserId());
            return ResponseEntity.ok(scoreTableDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }


    @GetMapping("/export-score-table")
    public ResponseEntity exportExcelScoreTableStudent(@RequestHeader("Access-Token") String accessToken) {
        try {
            // lấy theo token
            String token = "";
            if (accessToken != null && accessToken.length() > 6) {
                token = accessToken.substring(6);
            }
            UserPrincipal userPrincipal = jwtUtil.getUserFromToken(token);
            if (userPrincipal == null) {
                throw new ScheduleServiceException("Không tìm thấy user này.");
            }

            Workbook workbook = scoreTableService.exportScoreTable(userPrincipal.getUserId());
            String fileName = "lich-thi" + ".xlsx";
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition", "attachment;filename=" + fileName)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }
}
