package com.schedule.app.services.impl;

import com.schedule.app.converter.SubjectScheduleConverter;
import com.schedule.app.entities.*;
import com.schedule.app.models.dtos.subject_schedule.SubjectScheduleDTO;
import com.schedule.app.models.enums.FileStatus;
import com.schedule.app.repository.ICourseRegistrationResultRepository;
import com.schedule.app.repository.IScheduleFileRepository;
import com.schedule.app.repository.ISubjectScheduleRepository;
import com.schedule.app.repository.ISubjectScheduleResultRepository;
import com.schedule.app.services.ABaseServices;
import com.schedule.app.services.IScheduleFileService;
import com.schedule.app.services.ISubjectScheduleService;
import models.DateSchedule;
import models.Schedule;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectScheduleService extends ABaseServices implements ISubjectScheduleService {
    @Autowired
    ISubjectScheduleRepository subjectScheduleRepository;
    @Autowired
    IScheduleFileRepository scheduleFileRepository;

    @Autowired
    ISubjectScheduleResultRepository subjectScheduleResultRepository;

    @Autowired
    ICourseRegistrationResultRepository courseRegistrationResultRepository;

    @Autowired
    IScheduleFileService scheduleFileService;

    @Override
    public List<SubjectSchedule> getSubjectSchedulesByAcademyYear(int year) {
        return subjectScheduleRepository.getSubjectSchedulesByAcademyYear(year);
    }

    @Override
    public List<SubjectSchedule> getSubjectSchedules() {
        return subjectScheduleRepository.findAll();
    }
    @Override
    @Transactional
    public void setDefaultSubjectSchedule(String fileName) throws IOException, ClassNotFoundException {
        ScheduleFile scheduleFile=scheduleFileRepository.getScheduleFileByName(fileName);

        System.out.println(scheduleFile.getSemester().getId());
        Semester semester=scheduleFile.getSemester();

        subjectScheduleResultRepository.deleteSubjectScheduleResultBySemester(semester);
//        courseRegistrationResultRepository.deleteCourseRegistrationResultBySemester(semester);
        subjectScheduleRepository.deleteSubjectScheduleBySemester(semester);

        ByteArrayInputStream bis = new ByteArrayInputStream(scheduleFile.getFile());
        ObjectInputStream ois = new ObjectInputStream(bis);
        Schedule schedule = (Schedule) ois.readObject();
        ois.close();

        List<SubjectSchedule> subjectSchedules=new ArrayList<>();
                for(DateSchedule ds:schedule.getDateScheduleList()){
                    subjectSchedules.addAll( ds.getSubjectSchedules().stream().filter(item->item.getRoom().getCapacity()>0).map(ss->{
                        Subject subject=new Subject();
                        subject.setId(ss.getSubject().getId());
                        Classroom classroom=new Classroom();
                        classroom.setId(Long.parseLong(ss.getRoom().getRoom().getId()));
                        Course course=new Course();
                        course.setId((long) ss.getRoom().getRegistrationClass().getDbId());
                        return new SubjectSchedule(subject,classroom,course,ss.getShift(), LocalDate.parse(ds.getDate()),ss.getRoom().getIndex(),ss.getRoom().getCapacity());
                    }).collect(Collectors.toList()));
                }
        subjectScheduleRepository.saveAll(subjectSchedules);
        scheduleFileService.setFileUsedToStatus(FileStatus.DELETE);
        scheduleFile.setFileStatus(FileStatus.USED);
        scheduleFileRepository.save(scheduleFile);
    }
    @Override
    public Workbook exportSchedule(Long uid, int semester, int year) {
        // lấy ds lịch thi
        List<SubjectScheduleDTO> subjectScheduleDTO = new ArrayList<>();
        List<SubjectScheduleResult> subjectScheduleResults = subjectScheduleResultRepository.
                getSubjectScheduleByUserIdAndYearAndSemester(uid, year, semester);
        if (subjectScheduleResults != null && !subjectScheduleResults.isEmpty()) {
            subjectScheduleDTO = subjectScheduleResults.stream().map(subjectSchedule -> SubjectScheduleConverter.toSubjectScheduleDTO(subjectSchedule.getSubjectSchedule())).collect(Collectors.toList());
        }
        // create excel file
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("lich-thi");
        buildHeader(workbook, sheet);
        buildData(workbook, sheet, subjectScheduleDTO);
        return workbook;
    }

    private void buildData(XSSFWorkbook workbook, Sheet sheet, List<SubjectScheduleDTO> subjectSchedules) {

        XSSFFont font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setWrapText(false);
        style.setVerticalAlignment(VerticalAlignment.CENTER);


        int rowIndex = 1;
        for (SubjectScheduleDTO subjectSchedule : subjectSchedules) {
            int beginRow = rowIndex;
            Row row = sheet.createRow(beginRow);
            createCell(row, 0, beginRow, style);
            createCell(row, 1, subjectSchedule.getSubjectId(), style);
            createCell(row, 2, subjectSchedule.getSubjectName(), style);
            createCell(row, 3, subjectSchedule.getCourseName(), style);
            createCell(row, 4, subjectSchedule.getDateExam().toString(), style);
            createCell(row, 5, subjectSchedule.getClassroomName(), style);
            createCell(row, 6, subjectSchedule.getLessonStart(), style);
            createCell(row, 7, subjectSchedule.getLessonEnd(), style);
            createCell(row, 8, subjectSchedule.getExamType(), style);
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
        cell.setCellValue("Nhóm HP");

        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue("Ngày thi");

        cell = row.createCell(5);
        cell.setCellStyle(style);
        cell.setCellValue("Phòng thi");

        cell = row.createCell(6);
        cell.setCellStyle(style);
        cell.setCellValue("Tiết bắt đầu");

        cell = row.createCell(7);
        cell.setCellStyle(style);
        cell.setCellValue("Số tiết");

        cell = row.createCell(8);
        cell.setCellStyle(style);
        cell.setCellValue("Hình thức thi");
    }
}
