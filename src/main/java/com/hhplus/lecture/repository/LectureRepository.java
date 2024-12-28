package com.hhplus.lecture.repository;

import com.hhplus.lecture.entity.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT l FROM Lecture l WHERE l.id = :lectureId")
  Optional<Lecture> findByIdWithLock(@Param("lectureId") Long lectureId);

  @Query("SELECT l FROM Lecture l WHERE l.lectureTime BETWEEN :startOfDay " +
    "AND :endOfDay ORDER BY l.lectureTime")
  List<Lecture> findLecturesByDate(@Param("startOfDay") LocalDateTime startOfDay,
                                   @Param("endOfDay") LocalDateTime endOfDay);
}
