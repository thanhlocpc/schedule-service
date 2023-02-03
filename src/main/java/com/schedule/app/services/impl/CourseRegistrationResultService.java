package com.schedule.app.services.impl;

import com.schedule.app.entities.CourseRegistrationResult;
import com.schedule.app.models.dtos.score.ScoreTableDTO;
import com.schedule.app.models.dtos.score.SemesterTranscriptDTO;
import com.schedule.app.models.dtos.score.SubjectScoreDTO;
import com.schedule.app.services.ABaseServices;
import com.schedule.app.services.ICourseRegistrationResultService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author ThanhLoc
 * @created 2/2/2023
 */
@Service
public class CourseRegistrationResultService extends ABaseServices implements ICourseRegistrationResultService {
    @Override
    public List<CourseRegistrationResult> getCourseRegistrationResultByStudentIdAndYearAndSemester(Long studentId, int year, int semester) {
        return courseRegistrationResultRepository.findCourseRegistrationResultByStudentAndSemester(studentId,year,semester);
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
            double avgScore = 0;
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

                for (CourseRegistrationResult c : courses) {
                    totalScoreSub += c.getNumberScoreTen() * c.getCourse().getSubject().getCredit();
                    totalCreditSub += c.getCourse().getSubject().getCredit();
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
                semesterTranscriptDTO.setAvgScoreFour(Math.floor((avgScoreSub / 10) * 4 * 100) / 100);
                semesterTranscriptDTO.setTotalCredit(totalCreditPassSub);
                semesterTranscriptDTOS.add(semesterTranscriptDTO);

                //
                totalScore += totalScoreSub;
                totalCredit += totalCreditSub;
                totalCreditPass += totalCreditPassSub;
            }

            scoreTableDTO.setAvgScoreTen(Math.floor(((double) totalScore / totalCredit) * 100) / 100);
            scoreTableDTO.setAvgScoreFour(Math.floor(((double) totalScore / totalCredit) / 10 * 4 * 100) / 100);
            scoreTableDTO.setTotalCredit(totalCreditPass);
            return scoreTableDTO;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
