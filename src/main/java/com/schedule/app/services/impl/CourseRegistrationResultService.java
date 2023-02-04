package com.schedule.app.services.impl;

import com.schedule.app.converter.CourseRegistrationResultConverter;
import com.schedule.app.converter.SubjectScheduleConverter;
import com.schedule.app.entities.CourseRegistrationResult;
import com.schedule.app.entities.CourseTime;
import com.schedule.app.entities.SubjectScheduleResult;
import com.schedule.app.models.dtos.course_registration_result.CourseRegistrationResultDTO;
import com.schedule.app.models.dtos.score.ScoreTableDTO;
import com.schedule.app.models.dtos.score.SemesterTranscriptDTO;
import com.schedule.app.models.dtos.score.SubjectScoreDTO;
import com.schedule.app.models.dtos.subject_schedule.SubjectScheduleDTO;
import com.schedule.app.services.ABaseServices;
import com.schedule.app.services.ICourseRegistrationResultService;
import com.schedule.app.utils.DateUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author ThanhLoc
 * @created 2/2/2023
 */
@Service
public class CourseRegistrationResultService extends ABaseServices implements ICourseRegistrationResultService {
    @Override
    public List<CourseRegistrationResult> getCourseRegistrationResultByStudentIdAndYearAndSemester(Long studentId, int year, int semester) {
        return courseRegistrationResultRepository.findCourseRegistrationResultByStudentAndSemester(studentId, year, semester);
    }

    @Override
    public List<CourseRegistrationResult> findCourseRegistrationResultByStudent(Long studentId) {
        return courseRegistrationResultRepository.findCourseRegistrationResultByStudent(studentId);
    }

    @Override
    public ScoreTableDTO getScoreTableByStudent(Long userId) {
        try {
            List<CourseRegistrationResult> courseRegistrationResults = findCourseRegistrationResultByStudent(userId);

            Map<Integer, List<CourseRegistrationResult>> map = new TreeMap<>();
            for (CourseRegistrationResult c : courseRegistrationResults) {
                Integer key = c.getCourse().getSemester().getAcademyYear() * 10 + c.getCourse().getSemester().getSemesterName();
                if (map.get(key) != null) {
                    List<CourseRegistrationResult> courses = map.get(key);
                    courses.add(c);
                } else {
                    List<CourseRegistrationResult> courses = new ArrayList<>();
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
            for (Map.Entry<Integer, List<CourseRegistrationResult>> en : map.entrySet()) {
                List<CourseRegistrationResult> courses = en.getValue();
                SemesterTranscriptDTO semesterTranscriptDTO = new SemesterTranscriptDTO();
                semesterTranscriptDTO.setSemester(courses.get(0).getCourse().getSemester());
                List<SubjectScoreDTO> subjectScoreDTOS = new ArrayList<>();
                semesterTranscriptDTO.setSubjects(subjectScoreDTOS);
                double totalScoreSub = 0;
                double avgScoreSub = 0;
                int totalCreditSub = 0;
                int totalCreditPassSub = 0;
                int totalScoreFourSub = 0;


                for (CourseRegistrationResult c : courses) {
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
                semesterTranscriptDTO.setAvgScoreFour(Math.floor(((double)totalScoreFourSub / totalCreditSub) * 100) / 100);
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

    @Override
    public Workbook exportTimeTable(Long userId, int semester, int year) {
        // lấy ds học phần đăng kí
        List<CourseRegistrationResult> courseRegistrationResults = getCourseRegistrationResultByStudentIdAndYearAndSemester(userId, year, semester);
        List<CourseRegistrationResultDTO> courseRegistrationResultDTOS = courseRegistrationResults.stream().map(e -> CourseRegistrationResultConverter.toCourseRegistrationResultDTO(e)).collect(Collectors.toList());
        courseRegistrationResultDTOS.stream().forEach(e -> {
            e.getCourse().getCourseTimes().removeIf(item -> e.getCourseTimePractice() != null && item.getId() != e.getCourseTimePractice().getId() && item.getType().equals("TH"));
        });
        // create excel file
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("thoi-khoa-bieu");
        buildHeader(workbook, sheet);
        buildData(workbook, sheet, courseRegistrationResultDTOS);
        return workbook;
    }

    @Override
    public Workbook exportScoreTable(Long userId) {
        // lấy bảng điểm
        ScoreTableDTO scoreTableDTO = getScoreTableByStudent(userId);

        // create excel file
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("thoi-khoa-bieu");
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
            createCell(row, 0, "Học kì" + st.getSemester().getSemesterName() + " năm " + st.getSemester().getAcademyYear(), style);
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
                createCell(row, 7, ss.isPass() + "", style);
                beginRow++;
                rowIndex++;
            }
            rowIndex++;
        }
    }

    private void buildData(XSSFWorkbook workbook, Sheet sheet, List<CourseRegistrationResultDTO> courseRegistrationResultDTOS) {

        XSSFFont font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setWrapText(false);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        int rowIndex = 1;
        for (CourseRegistrationResultDTO c : courseRegistrationResultDTOS) {
            for (CourseTime courseTime : c.getCourse().getCourseTimes()) {
                int beginRow = rowIndex;
                Row row = sheet.createRow(rowIndex);
                createCell(row, 0, beginRow, style);
                createCell(row, 1, c.getCourse().getSubject().getId(), style);
                createCell(row, 2, c.getCourse().getSubject().getName(), style);
                createCell(row, 3, c.getCourse().getSubject().getCredit(), style);
                createCell(row, 4, c.getCourse().getClassEntity().getName(), style);
                createCell(row, 5, courseTime.getDayOfWeek(), style);
                createCell(row, 6, courseTime.getTimeStart(), style);
                createCell(row, 7, c.getCourse().getSubject().getLessonTime(), style);
                createCell(row, 8, courseTime.getClassroom().getName(), style);
                createCell(row, 9, DateUtils.dateToString(courseTime.getDateStart()) + "-" + DateUtils.dateToString(courseTime.getDateEnd()), style);
                rowIndex++;
            }
        }
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

    private void buildHeader(XSSFWorkbook workbook, Sheet sheet) {
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
        cell.setCellValue("Lớp");

        cell = row.createCell(5);
        cell.setCellStyle(style);
        cell.setCellValue("Thứ");

        cell = row.createCell(6);
        cell.setCellStyle(style);
        cell.setCellValue("Tiết bắt đầu");

        cell = row.createCell(7);
        cell.setCellStyle(style);
        cell.setCellValue("Số tiết");

        cell = row.createCell(8);
        cell.setCellStyle(style);
        cell.setCellValue("Phòng");

        cell = row.createCell(9);
        XSSFCellStyle f = (XSSFCellStyle) style.clone();
        f.setWrapText(true);
        cell.setCellStyle(f);
        cell.setCellValue("Thời gian học");
    }
}
