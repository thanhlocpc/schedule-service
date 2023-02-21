package com.schedule.app.services.impl;

import com.schedule.app.entities.CourseRegistrationResult;
import com.schedule.app.entities.ScoreTable;
import com.schedule.app.models.dtos.score.ScoreTableDTO;
import com.schedule.app.models.dtos.score.SemesterTranscriptDTO;
import com.schedule.app.models.dtos.score.SubjectScoreDTO;
import com.schedule.app.services.ABaseServices;
import com.schedule.app.services.IScoreTableService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author ThanhLoc
 * @created 2/17/2023
 */
@Service
public class ScoreTableService extends ABaseServices implements IScoreTableService {
    @Override
    public ScoreTableDTO findScoreTableByStudent(Long studentId) {
//        return scoreRepository.findScoreTableByStudent(studentId);
        try {
            List<ScoreTable> scoreTables = scoreRepository.findScoreTableByStudent(studentId);

            Map<Integer, List<ScoreTable>> map = new TreeMap<>();
            for (ScoreTable c : scoreTables) {
                Integer key = c.getCourse().getSemester().getAcademyYear() * 10 + c.getCourse().getSemester().getSemesterName();
                if (map.get(key) != null) {
                    List<ScoreTable> courses = map.get(key);
                    courses.add(c);
                } else {
                    List<ScoreTable> courses = new ArrayList<>();
                    courses.add(c);
                    map.put(key, courses);
                }
            }
            List<SemesterTranscriptDTO> semesterTranscriptDTOS = new ArrayList<>();
            ScoreTableDTO scoreTableDTO = new ScoreTableDTO();
            scoreTableDTO.setSemesterTranscripts(semesterTranscriptDTOS);
            double totalScore = 0;
            double totalScoreFour = 0;
            int totalCredit = 0;
            int totalCreditPass = 0;
            for (Map.Entry<Integer, List<ScoreTable>> en : map.entrySet()) {
                List<ScoreTable> courses = en.getValue();
                SemesterTranscriptDTO semesterTranscriptDTO = new SemesterTranscriptDTO();
                semesterTranscriptDTO.setSemester(courses.get(0).getCourse().getSemester());
                List<SubjectScoreDTO> subjectScoreDTOS = new ArrayList<>();
                semesterTranscriptDTO.setSubjects(subjectScoreDTOS);
                double totalScoreSub = 0;
                double avgScoreSub = 0;
                int totalCreditSub = 0;
                int totalCreditPassSub = 0;
                int totalScoreFourSub = 0;


                for (ScoreTable c : courses) {
                    totalScoreSub += c.getNumberScoreTen() * c.getCourse().getSubject().getCredit();
                    totalCreditSub += c.getCourse().getSubject().getCredit();
                    totalScoreFourSub += c.getNumberScoreFour() * c.getCourse().getSubject().getCredit();
                    SubjectScoreDTO subjectScoreDTO = new SubjectScoreDTO();
                    if (c.getNumberScoreTen() >= 4) {
                        totalCreditPassSub += c.getCourse().getSubject().getCredit();
                        subjectScoreDTO.setPass(true);
                    }
                    subjectScoreDTO.setSubject(c.getCourse().getSubject());
                    subjectScoreDTO.setNumberScoreTen(c.getNumberScoreTen());
                    subjectScoreDTO.setNumberScoreFour(c.getNumberScoreFour());
                    subjectScoreDTO.setLiteralScore(c.getLiteralScore());
                    subjectScoreDTOS.add(subjectScoreDTO);
                }
                avgScoreSub = Math.floor(((double) totalScoreSub / totalCreditSub) * 100) / 100;
                semesterTranscriptDTO.setAvgScoreTen(avgScoreSub);
                semesterTranscriptDTO.setAvgScoreFour(Math.floor(((double) totalScoreFourSub / totalCreditSub) * 100) / 100);
                semesterTranscriptDTO.setTotalCredit(totalCreditPassSub);
                semesterTranscriptDTOS.add(semesterTranscriptDTO);

                //
                totalScore += totalScoreSub;
                totalCredit += totalCreditSub;
                totalCreditPass += totalCreditPassSub;
                totalScoreFour += semesterTranscriptDTO.getAvgScoreFour();

                // end 1 học kì
            }

            scoreTableDTO.setAvgScoreTen(Math.floor(((double) totalScore / totalCredit) * 100) / 100);
            scoreTableDTO.setAvgScoreFour(Math.floor(((double) totalScoreFour / scoreTableDTO.getSemesterTranscripts().size()) * 100) / 100);
            scoreTableDTO.setTotalCredit(totalCreditPass);
            return scoreTableDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //    @Override
    public Workbook exportScoreTable(Long userId) {
        // lấy bảng điểm
        ScoreTableDTO scoreTableDTO = findScoreTableByStudent(userId);

        // create excel file
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Bảng điểm");
        buildHeaderScoreTable(workbook, sheet);
        buildDataScoreTable(workbook, sheet, scoreTableDTO);

        return workbook;
    }

    private void buildHeaderScoreTable(XSSFWorkbook workbook, Sheet sheet) {
        Row row = sheet.createRow(0);
        XSSFCellStyle style = workbook.createCellStyle();
//        style.setFillForegroundColor(new XSSFColor(new Color(72, 206, 58)));
        style.setAlignment(HorizontalAlignment.CENTER);
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("STT");

        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("Mã MH");

        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("Tên MH");

        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue("TC");

        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue("Điểm hệ 10");

        cell = row.createCell(5);
        cell.setCellStyle(style);
        cell.setCellValue("Điểm hệ 4");

        cell = row.createCell(6);
        cell.setCellStyle(style);
        cell.setCellValue("Điểm chữ");

        cell = row.createCell(7);
        cell.setCellStyle(style);
        cell.setCellValue("Kết quả");
    }

    private void buildDataScoreTable(XSSFWorkbook workbook, Sheet sheet, ScoreTableDTO scoreTableDTO) {

        XSSFFont font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setWrapText(false);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        int rowIndex = 1;
        for (SemesterTranscriptDTO st : scoreTableDTO.getSemesterTranscripts()) {
            Row row = sheet.createRow(rowIndex++);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 7));
            createCell(row, 0, "Học kì " + st.getSemester().getSemesterName() + " năm " + st.getSemester().getAcademyYear(), style);
            int beginRow = 1;
            for (SubjectScoreDTO ss : st.getSubjects()) {
                row = sheet.createRow(rowIndex);
                createCell(row, 0, beginRow, style);
                createCell(row, 1, ss.getSubject().getId(), style);
                createCell(row, 2, ss.getSubject().getName(), style);
                createCell(row, 3, ss.getSubject().getCredit(), style);
                createCell(row, 4, ss.getNumberScoreTen(), style);
                createCell(row, 5, ss.getNumberScoreFour(), style);
                createCell(row, 6, ss.getLiteralScore(), style);
                createCell(row, 7, ss.isPass() ? "Đạt" : "Trượt", style);
                beginRow++;
                rowIndex++;
            }
            rowIndex++;
        }
        // build điểm trung bình
        rowIndex++;
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 7));
        style.setAlignment(HorizontalAlignment.RIGHT);
        Row row = sheet.createRow(rowIndex++);
        createCell(row, 0, "Tín chỉ tích lũy: " + scoreTableDTO.getTotalCredit(), style);

        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 7));
        row = sheet.createRow(rowIndex++);
        createCell(row, 0, "Điểm trung bình hệ 10: " + scoreTableDTO.getAvgScoreTen(), style);

        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 7));
        row = sheet.createRow(rowIndex++);
        createCell(row, 0, "Điểm trung bình hệ 4: " + scoreTableDTO.getAvgScoreFour(), style);
    }

    private void createCell(Row row, int column, Object data, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellStyle(style);
        if (data instanceof String) {
            cell.setCellValue((String) data);
        } else if (data instanceof Double) {
            cell.setCellValue((Double) data);
        } else if (data instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) data).longValue());
        } else if (data instanceof Integer) {
            cell.setCellValue((Integer) data);
        }
    }
}
