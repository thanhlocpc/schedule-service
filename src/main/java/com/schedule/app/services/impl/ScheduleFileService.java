package com.schedule.app.services.impl;

import com.schedule.app.entities.ScheduleFile;
import com.schedule.app.entities.Semester;
import com.schedule.app.models.dtos.schedule_file.ScheduleFileDTO;
import com.schedule.app.models.enums.EnumsConst;
import com.schedule.app.models.enums.FileStatus;
import com.schedule.app.repository.IScheduleFileRepository;
import com.schedule.app.services.IScheduleFileService;
import com.schedule.initialization.data.InitData;
import com.schedule.initialization.gwo.GWO;
import com.schedule.initialization.models.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public Schedule generateNewSchedule(List<Integer> properties) throws IOException, InterruptedException, CloneNotSupportedException, ClassNotFoundException {
        List<String> dates = InitData.examDates;
        GWO gwo = new GWO(dates,properties);
        byte[] scheduleByteArray = gwo.generateNewSchedule(1);
        Semester semester = new Semester(1, 2021);
        semester.setId(1L);
        addScheduleFile(new ScheduleFile(LocalDateTime.now().toString(), scheduleByteArray, FileStatus.NEW, semester));

        Schedule schedule = gwo.convertByteToSchedule(scheduleByteArray);
        return schedule;

    }

    @Override
    public ScheduleFile getUsedScheduleFile() {
        return scheduleFileRepository.getUsedScheduleFile();
    }

    @Override
    public ScheduleFile getScheduleFileByFileName(String fileName) {
        System.out.println(fileName);
        return scheduleFileRepository.getScheduleFileByName(fileName);
    }

    @Override
    public List<ScheduleFileDTO> getAllScheduleFile() {

        return scheduleFileRepository.findAll().stream().map(item -> new ScheduleFileDTO(item.getFileName(), item.getFileStatus())).collect(Collectors.toList());
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
        if (scheduleAfterChangeByteArray != null) {
            ByteArrayInputStream bisAC = new ByteArrayInputStream(scheduleAfterChangeByteArray);
            ObjectInputStream oisAC = new ObjectInputStream(bisAC);
            scheduleAfterChange = (Schedule) oisAC.readObject();
            ois.close();
            scheduleFileRepository.save(scheduleFile);
            addScheduleFile(new ScheduleFile(LocalDateTime.now().toString(), scheduleAfterChangeByteArray, FileStatus.CHANGE, scheduleFile.getSemester()));
        }

        return scheduleAfterChange;
    }
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Schedule changeSubjectSchedule(List<ChangeSubjectScheduleRequest> changeSubjectScheduleRequests) throws IOException, ClassNotFoundException, CloneNotSupportedException {
        ScheduleFile scheduleFile = getUsedScheduleFile();

        ByteArrayInputStream bis = new ByteArrayInputStream(scheduleFile.getFile());
        ObjectInputStream ois = new ObjectInputStream(bis);
        Schedule schedule = (Schedule) ois.readObject();
        ois.close();
        GWO gwo = new GWO();
        byte[] scheduleAfterChangeByteArray = gwo.changeSubjectSchedule(changeSubjectScheduleRequests, schedule);
        Schedule scheduleAfterChange = null;
        if (scheduleAfterChangeByteArray != null) {
            ByteArrayInputStream bisAC = new ByteArrayInputStream(scheduleAfterChangeByteArray);
            ObjectInputStream oisAC = new ObjectInputStream(bisAC);
            scheduleAfterChange = (Schedule) oisAC.readObject();
            ois.close();
            scheduleFileRepository.save(scheduleFile);
            addScheduleFile(new ScheduleFile(LocalDateTime.now().toString(), scheduleAfterChangeByteArray, FileStatus.CHANGE, scheduleFile.getSemester()));
        }

        return scheduleAfterChange;
    }
    @Override
    public Workbook exportSchedule(Long uid, int semester, int year, Schedule schedule) {

        // create excel file
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("lich-thi");
        buildHeader(workbook, sheet);
        buildData(workbook, sheet, schedule.getDateScheduleList());
        return workbook;
    }

    @Override
    public void setFileUsedToStatus(FileStatus fileStatus) {
        ScheduleFile scheduleFile = getUsedScheduleFile();
        scheduleFile.setFileStatus(fileStatus);
        scheduleFileRepository.save(scheduleFile);
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
        for (DateSchedule dateSchedule : dateSchedules)
            for (SubjectSchedule subjectSchedule : dateSchedule.getSubjectSchedules()) {
                if (subjectSchedule.getRoom().getCapacity() != 0) {
                    int beginRow = rowIndex;
                    Row row = sheet.createRow(beginRow);
                    createCell(row, 0, beginRow, style);
                    createCell(row, 1, subjectSchedule.getSubject().getId(), style);
                    createCell(row, 2, subjectSchedule.getSubject().getName(), style);
                    createCell(row, 3, dateSchedule.getDate().toString(), style);
                    createCell(row, 4, subjectSchedule.getRoom().getRoom().getName(), style);
            createCell(row, 5, subjectSchedule.getShift()*3+1, style);
            createCell(row, 6, subjectSchedule.getRoom().getCapacity(), style);
                    createCell(row, 7, types.get(subjectSchedule.getSubject().getExamForms()), style);
                    rowIndex++;
                }
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
        cell.setCellValue("Số thí sinh");

        cell = row.createCell(7);
        cell.setCellStyle(style);
        cell.setCellValue("Hình thức thi");
    }
}
