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
import java.util.*;
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
    public Workbook exportTimeTable(Long userId, int semester, int year) {
        // lấy ds học phần đăng kí
        List<CourseRegistrationResult> courseRegistrationResults = getCourseRegistrationResultByStudentIdAndYearAndSemester(userId, year, semester);
        List<CourseRegistrationResultDTO> courseRegistrationResultDTOS = courseRegistrationResults.stream().map(e -> CourseRegistrationResultConverter.toCourseRegistrationResultDTO(e)).collect(Collectors.toList());
        courseRegistrationResultDTOS.stream().forEach(e -> {
            List<String> courseTimePractices = Arrays.stream(e.getCourseTimePractices().split(",")).collect(Collectors.toList());
            e.getCourse().getCourseTimes().removeIf(item -> e.getCourseTimePractices() != null &&
                    !courseTimePractices.contains(item.getId() + "") &&
                    item.getType().equals("TH"));
        });
        // create excel file
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("thoi-khoa-bieu");
        buildHeader(workbook, sheet);
        buildData(workbook, sheet, courseRegistrationResultDTOS);
        return workbook;
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
