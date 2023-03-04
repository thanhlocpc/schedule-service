package com.schedule.app.controller;

import com.schedule.app.converter.SubjectScheduleConverter;
import com.schedule.app.entities.ScheduleFile;
import com.schedule.app.entities.SubjectScheduleResult;
import com.schedule.app.handler.ScheduleServiceException;
import com.schedule.app.models.dtos.subject_schedule.SubjectScheduleDTO;
import com.schedule.app.models.enums.EnumsConst;
import com.schedule.app.models.wrapper.ObjectResponseWrapper;
import com.schedule.app.repository.IScheduleFileRepository;
import com.schedule.app.repository.ISubjectScheduleRepository;
import com.schedule.app.requests.GenerateScheduleRequest;
import com.schedule.app.security.UserPrincipal;
import com.schedule.app.services.IScheduleFileService;
import com.schedule.app.services.ISubjectScheduleService;
import com.schedule.app.utils.Constants;
import com.schedule.app.utils.Extensions;
import com.schedule.initialization.data.InitData;
import com.schedule.initialization.gwo.GWO;
import com.schedule.initialization.models.*;
import com.schedule.initialization.utils.ExcelFile;
import io.swagger.v3.oas.annotations.Operation;
import lombok.experimental.ExtensionMethod;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.IBody;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtensionMethod(Extensions.class)
@RestController
@RequestMapping(Constants.SUBJECT_SCHEDULE_SERVICE_URL)
public class SubjectScheduleController extends BaseAPI {
    @Autowired
    ISubjectScheduleService subjectScheduleService;

    @Autowired
    IScheduleFileService scheduleFileService;
    @Autowired
    private ISubjectScheduleRepository iSubjectScheduleRepository;

    @PostMapping("/change")
    public ResponseEntity changeSchedule(@RequestBody List<ChangeScheduleRequest> changeSchedules) throws IOException, ClassNotFoundException, CloneNotSupportedException {
        Schedule scheduleChange = scheduleFileService.changeSchedule(changeSchedules);
        if (scheduleChange == null) return ResponseEntity.badRequest().body(null);
        else return ResponseEntity.ok(scheduleChange);
    }
    @PostMapping("/changeSubjectSchedule")
    public ResponseEntity changeSubjectSchedule(@RequestBody List<ChangeSubjectScheduleRequest> changeSubjectSchedules) throws IOException, ClassNotFoundException, CloneNotSupportedException {
        Schedule scheduleChange = scheduleFileService.changeSubjectSchedule(changeSubjectSchedules);
        if (scheduleChange == null) return ResponseEntity.badRequest().body(null);
        else return ResponseEntity.ok(scheduleChange);
    }

    @PostMapping("/as-default/{fileName}")
    public ResponseEntity setDefaultSchedule(@PathVariable("fileName") String fileName) throws IOException, ClassNotFoundException {
        subjectScheduleService.setDefaultSubjectSchedule(fileName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/using/{academyYear}")
    public List<SubjectScheduleDTO> getSubjectSchedulesByAcademyYear(@PathVariable(name = "academyYear") Integer year) {
        List<com.schedule.app.entities.SubjectSchedule> subjectSchedules = subjectScheduleService.getSubjectSchedulesByAcademyYear(year);
//        List<SubjectSchedule> subjectSchedules = subjectScheduleService.getSubjectSchedules();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(com.schedule.app.entities.SubjectSchedule.class, SubjectScheduleDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getSubject().getName(), SubjectScheduleDTO::setSubjectName);
            mapper.map(src -> src.getClassroom().getName(), SubjectScheduleDTO::setClassroomName);
            mapper.map(src -> src.getCourse().getName(), SubjectScheduleDTO::setCourseName);
            mapper.map(src -> src.getCourse().getSemester().getSemesterName(), SubjectScheduleDTO::setSemesterName);
            mapper.map(src -> src.getCourse().getClassEntity().getName(), SubjectScheduleDTO::setClassName);
            mapper.map(src -> src.getCourse().getSemester().getAcademyYear(), SubjectScheduleDTO::setAcademyYear);
            mapper.map(src -> src.getCandidateAmount(), SubjectScheduleDTO::setCandidateAmount);
            mapper.map(src -> src.getSubjectScheduleIndex(), SubjectScheduleDTO::setSubjectScheduleIndex);
        });
        List<SubjectScheduleDTO> subjectScheduleDTO = subjectSchedules.stream().filter(item -> item.getCandidateAmount() > 0).map(subjectSchedule -> modelMapper.map(subjectSchedule, SubjectScheduleDTO.class)).collect(Collectors.toList());
        return subjectScheduleDTO;
    }

    @GetMapping("/{fileName}")
    public List<SubjectScheduleDTO> getSubjectSchedules(@PathVariable("fileName") String fileName) throws IOException, ClassNotFoundException {
        System.out.println("controller " + fileName);
        ScheduleFile scheduleFile = scheduleFileService.getScheduleFileByFileName(fileName);
        ByteArrayInputStream bis = new ByteArrayInputStream(scheduleFile.getFile());
        ObjectInputStream ois = new ObjectInputStream(bis);
        Schedule schedule = (Schedule) ois.readObject();
        ois.close();
//        List<SubjectSchedule> subjectSchedules = subjectScheduleService.getSubjectSchedulesByAcademyYear(year);
        List<DateSchedule> dateScheduleList = schedule.getDateScheduleList();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(SubjectSchedule.class, SubjectScheduleDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getSubject().getName(), SubjectScheduleDTO::setSubjectName);
            mapper.map(src -> src.getRoom().getRoom().getName(), SubjectScheduleDTO::setClassroomName);
            mapper.map(src -> src.getRoom().getRegistrationClass().getName(), SubjectScheduleDTO::setCourseName);
            mapper.map(src -> "1", SubjectScheduleDTO::setSemesterName);
            mapper.map(src -> "2019", SubjectScheduleDTO::setAcademyYear);
            mapper.map(src -> src.getRoom().getRegistrationClass().getGrade().getName(), SubjectScheduleDTO::setClassName);
            mapper.map(src -> (src.getShift() + 1), SubjectScheduleDTO::setShift);
        });
        List<String> types = Stream.of(EnumsConst.ExamType.values()).map(Enum::name).collect(Collectors.toList());
        List<SubjectScheduleDTO> subjectScheduleDTOs = new ArrayList<>();
        dateScheduleList.forEach(item -> {
            item.getSubjectSchedules().forEach(ss -> {
                if (ss.getRoom().getCapacity() > 0) {
                    SubjectScheduleDTO ssDto = modelMapper.map(ss, SubjectScheduleDTO.class);
                    ssDto.setDateExam(LocalDate.parse(item.getDate()));
                    ssDto.setExamType(types.get(ss.getSubject().getExamForms()));
                    subjectScheduleDTOs.add(ssDto);
                }
            });
        });
        System.out.println("========schedule actual");
        List<DateSchedule> dses = schedule.getDateScheduleList();

        for (int i = 0; i < dses.size(); i++) {
            System.out.println(dses.get(i).toString());
        }
//        List<SubjectScheduleDTO> subjectScheduleDTO = subjectSchedules.stream().map(subjectSchedule -> modelMapper.map(subjectSchedule, SubjectScheduleDTO.class)).collect(Collectors.toList());
        return subjectScheduleDTOs;
    }

    @PostMapping("/schedule-excel")
    @Operation(summary = "Tạo lịch thi bằng excel")
    public ObjectResponseWrapper createScheduleByExcel(@RequestParam("file") MultipartFile file,
                                               @RequestParam("properties") String properties) throws IOException {

        try {
            // code tạo lịch thi bằng excel ở đây
            InputStream inputStream = file.getInputStream();
            // đọc nội dung file excel
            Workbook workbook = new XSSFWorkbook(inputStream);
            ExcelFile.setWb(workbook);
            InitData.initData();
            scheduleFileService.generateNewSchedule(Arrays.stream(properties.split(","))
                    .map(e->Integer.parseInt(e)).collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
            return ObjectResponseWrapper.builder()
                    .status(0)
                    .message("Vui lòng thử lại.")
                    .build();
        }


        return ObjectResponseWrapper.builder()
                .status(1)
                .message("Đã tạo một lịch thi mới")
                .build();
    }

    @GetMapping("/student")
    public ResponseEntity getSubjectScheduleByStudent(@RequestParam("year") int year, @RequestParam("semester") int semester, @RequestHeader("Access-Token") String accessToken) {

        // lấy theo token
        String token = "";
        if (accessToken != null && accessToken.length() > 6) {
            token = accessToken.substring(6);
        }
        UserPrincipal userPrincipal = jwtUtil.getUserFromToken(token);
        if (userPrincipal == null) {
            throw new ScheduleServiceException("Không tìm thấy user này.");
        }

        // lấy ds lịch thi
        List<SubjectScheduleResult> subjectScheduleResults = courseRegisterResultRepository.getSubjectScheduleByUserIdAndYearAndSemester(userPrincipal.getUserId(), year, semester);
        if (subjectScheduleResults != null && !subjectScheduleResults.isEmpty()) {
            List<SubjectScheduleDTO> subjectScheduleDTO = subjectScheduleResults.stream().map(subjectSchedule -> SubjectScheduleConverter.toSubjectScheduleDTO(subjectSchedule.getSubjectSchedule())).collect(Collectors.toList());
            return ResponseEntity.ok(subjectScheduleDTO);
        }
        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }

    @GetMapping("/export-schedule")
    public ResponseEntity exportExcelScheduleStudent(@RequestParam("year") int year, @RequestParam("semester") int semester, @RequestHeader("Access-Token") String accessToken) {
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

            Workbook workbook = subjectScheduleService.exportSchedule(userPrincipal.getUserId(), semester, year);
            String fileName = "lich-thi" + ".xlsx";
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment;filename=" + fileName).body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }


}
