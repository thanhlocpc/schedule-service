package com.schedule.app.repository;

import com.schedule.app.entities.ScoreTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IScoreTableRepository extends JpaRepository<ScoreTable,Long> {
    @Query("select distinct c from ScoreTable c where c.user.id = :studentId")
    public List<ScoreTable> findScoreTableByStudent(@Param("studentId") Long studentId);
}
