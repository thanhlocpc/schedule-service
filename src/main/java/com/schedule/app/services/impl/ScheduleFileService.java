package com.schedule.app.services.impl;

import com.schedule.app.converter.SubjectScheduleConverter;
import com.schedule.app.entities.ScheduleFile;
import com.schedule.app.entities.SubjectScheduleResult;
import com.schedule.app.models.dtos.subject_schedule.SubjectScheduleDTO;
import com.schedule.app.models.enums.EnumsConst;
import com.schedule.app.models.enums.FileStatus;
import com.schedule.app.repository.IScheduleFileRepository;
import com.schedule.app.services.IScheduleFileService;
import gwo.GWO;
import models.ChangeScheduleRequest;
import models.DateSchedule;
import models.Schedule;
import models.SubjectSchedule;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ScheduleFileService implements IScheduleFileService {
    @Autowired
    IScheduleFileRepository scheduleFileRepository;

    @Override
    public void addScheduleFile(ScheduleFile scheduleFile) {
        scheduleFileRepository.save(scheduleFile);
    }

    @Override
    public Schedule generateNewSchedule() throws IOException, InterruptedException, CloneNotSupportedException, ClassNotFoundException {
        List<String> dates = new ArrayList<>();
        dates.add("2022-10-12");
        dates.add("2022-10-13");
        dates.add("2022-10-14");
        dates.add("2022-10-15");
        dates.add("2022-10-16");
        dates.add("2022-10-17");
        dates.add("2022-10-18");
        dates.add("2022-10-19");
        dates.add("2022-10-20");
        GWO gwo = new GWO(dates);
        byte[] scheduleByteArray = gwo.generateNewSchedule(1);
        addScheduleFile(new ScheduleFile(scheduleByteArray, FileStatus.NEW));

        Schedule schedule=gwo.convertByteToSchedule(scheduleByteArray);
      return schedule;

    }

    @Override
    public ScheduleFile getUsedScheduleFile() {
        return scheduleFileRepository.getUsedScheduleFile();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Schedule changeSchedule(List<ChangeScheduleRequest> changeScheduleRequests) throws IOException, ClassNotFoundException, CloneNotSupportedException {
        ScheduleFile scheduleFile = getUsedScheduleFile();

        ByteArrayInputStream bis = new ByteArrayInputStream(scheduleFile.getFile());
        ObjectInputStream ois = new ObjectInputStream(bis);
        Schedule schedule = (Schedule) ois.readObject();
        ois.close();
        GWO gwo = new GWO();
        byte[] scheduleAfterChangeByteArray = gwo.changeSchedule(changeScheduleRequests, schedule);
        Schedule scheduleAfterChange = null;
        if (scheduleAfterChangeByteArray!=null) {
            ByteArrayInputStream bisAC = new ByteArrayInputStream(scheduleAfterChangeByteArray);
            ObjectInputStream oisAC = new ObjectInputStream(bisAC);
            scheduleAfterChange = (Schedule) oisAC.readObject();
            ois.close();
            scheduleFile.setFileStatus(FileStatus.DELETE);
            scheduleFileRepository.save(scheduleFile);

            addScheduleFile(new ScheduleFile(scheduleAfterChangeByteArray,FileStatus.USED));
        }

        return scheduleAfterChange;
    }
    @Override
    public Workbook exportSchedule(Long uid, int semester, int year,Schedule schedule) {

        // create excel file
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("lich-thi");
        buildHeader(workbook, sheet);
        buildData(workbook, sheet, schedule.getDateScheduleList());
        return workbook;
    }
    private void buildData(XSSFWorkbook workbook, Sheet sheet, List<DateSchedule> dateSchedules) {

        XSSFFont font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setWrapText(false);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        List<String> types = Stream.of(EnumsConst.ExamType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        int rowIndex = 1;
        for (DateSchedule dateSchedule:dateSchedules)
        for (SubjectSchedule subjectSchedule : dateSchedule.getSubjectSchedules()) {
            int beginRow = rowIndex;
            Row row = sheet.createRow(beginRow);
            createCell(row, 0, beginRow, style);
            createCell(row, 1, subjectSchedule.getSubject().getId(), style);
            createCell(row, 2, subjectSchedule.getSubject().getName(), style);
            createCell(row, 3, dateSchedule.getDate().toString(), style);
            createCell(row, 4, subjectSchedule.getRoom().getRoom().getName(), style);
//            createCell(row, 5, subjectSchedule.getSubject().get, style);
//            createCell(row, 6, subjectSchedule.getLessonEnd(), style);
            createCell(row, 7, types.get(subjectSchedule.getSubject().getExamForms()), style);
            rowIndex++;
        }
    }
    private void createCell(Row row, int column, Object data, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellStyle(style);
        if (data instanceof String) {
            cell.setCellValue((String) data);
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
        cell.setCellValue("Ngày thi");

        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue("Phòng thi");

        cell = row.createCell(5);
        cell.setCellStyle(style);
        cell.setCellValue("Tiết bắt đầu");

        cell = row.createCell(6);
        cell.setCellStyle(style);
        cell.setCellValue("Số tiết");

        cell = row.createCell(7);
        cell.setCellStyle(style);
        cell.setCellValue("Hình thức thi");
    }
}
