package com.schedule.app.repository;

import com.schedule.app.entities.Semester;
import com.schedule.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author : Thành Lộc
 * @since : 10/12/2022, Wed
 **/
public interface ISemesterRepository extends JpaRepository<Semester, Long>, IRepositoryUserCustom {
    Semester findSemesterByAcademyYearAndSemesterName(int year, int name);

}
