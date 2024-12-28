package com.hhplus.lecture.repository;

import com.hhplus.lecture.entity.Lecture;
import com.hhplus.lecture.entity.Registration;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
  @Lock(LockModeType.PESSIMISTIC_READ)
  int countByLecture(Lecture lecture);

  boolean existsByLectureAndUserId(Lecture lecture, Long userId);

  List<Registration> findByUserId(Long userId);
}
